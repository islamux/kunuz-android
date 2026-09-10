# Kunuz Android Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Full-parity native Android port of the Kunuz web app (140 hadith encyclopedia) in Kotlin + Jetpack Compose.

**Architecture:** Single-Activity Compose app, state-driven tabs + dialogs (mirrors `App.tsx` — no navigation library). JSON data assets → repositories (DataStore persistence) → one `KunuzViewModel` → composables. All search/streak/share/pick logic lives in pure Kotlin `logic/` files, unit-tested by direct ports of the web Vitest suite.

**Tech Stack:** Kotlin 2.2.x, Jetpack Compose (BOM, Material 3), AGP 8.13.x / Gradle Kotlin DSL + version catalog, kotlinx-serialization, DataStore Preferences, coroutines/StateFlow, `com.composables:icons-lucide` (fallback: material-icons-extended), JUnit + Robolectric + Compose UI tests. JDK 17.

**Spec:** `docs/superpowers/specs/2026-09-10-kunuz-android-design.md`

## Global Constraints

- Project root: `/media/islamux/Variety/JavaScriptProjects/kunuz-android` — separate git repo; never edit web repo files except `scripts/export-data.ts` (Task 2)
- Branch: `feat/android-port`; license GPL-3.0-or-later (copy web repo's LICENSE)
- appId/package: `com.islamux.kunuz`; minSdk 26, targetSdk/compileSdk 36
- Forced RTL layout (Arabic-first app, like the web); light theme only
- Data keys match web semantics: `sunnah-favorites`, `sunnah-streak`, `sunnah-streak-last`, `sunnah-tasks-<yyyy-MM-dd>`
- Every task: tests written first (failing), then implementation, `./gradlew :app:testDebugUnitTest` green before commit
- Reference source files are in `/media/islamux/Variety/JavaScriptProjects/kunuz/src/` (path `web:` below)

## File Structure (end state)

```
kunuz-android/
├── settings.gradle.kts, build.gradle.kts, gradle/libs.versions.toml
├── LICENSE, README.md, .gitignore
├── scripts/export-data.ts            # lives in WEB repo instead (Task 2)
└── app/
    ├── build.gradle.kts, proguard-rules.pro
    └── src/
        ├── main/
        │   ├── AndroidManifest.xml
        │   ├── assets/data/{treasures.json, chapters.json}
        │   ├── res/font/{amiri_regular,bold}.ttf, tajawal_{regular,medium,bold}.ttf
        │   ├── res/{values, mipmap-anydpi-v26, drawable}
        │   └── java/com/islamux/kunuz/
        │       ├── KunuzApplication.kt        # AppContainer (manual DI)
        │       ├── MainActivity.kt
        │       ├── data/model/Models.kt
        │       ├── data/TreasuresRepository.kt
        │       ├── data/FavoritesRepository.kt
        │       ├── data/DailyTasksRepository.kt
        │       ├── data/SettingsRepository.kt
        │       ├── logic/ArabicText.kt, Filters.kt, ShareFormatter.kt, Streak.kt, Dates.kt
        │       ├── speech/ArabicTtsController.kt, SpeechText.kt
        │       ├── share/AndroidSharer.kt
        │       ├── sound/ClickTone.kt, Confetti.kt
        │       ├── ui/theme/{Theme.kt, Type.kt, Color.kt}
        │       ├── ui/KunuzViewModel.kt, KunuzApp.kt
        │       └── ui/components/…
        └── test/java/com/islamux/kunuz/
```

---

### Task 1: Bootstrap project (Gradle + Compose + theme + fonts + RTL)

**Files:** Create root Gradle files, `app/build.gradle.kts`, manifest, `KunuzApplication.kt`, `MainActivity.kt`, `ui/theme/*`, font files, LICENSE, .gitignore.
**Produces:** `KunuzTheme {}` wrapper; `Type.kt` with `Amiri`/`Tajawal` FontFamilies; `LocalKunuzTextScale: ProvidableCompositionLocal<Float>` (1.0f/1.15f/1.3f).

1. Init git branch `feat/android-port`, commit plan + spec.
2. Scaffold version catalog (kotlin 2.2.x, AGP 8.13.x, compose BOM, kotlinx-serialization, datastore, lifecycle-viewmodel-compose, test deps).
3. Download fonts (SIL OFL): amiri/Amiri-Regular.ttf, Amiri-Bold.ttf, tajawal/Tajawal-{Regular,Medium,Bold}.ttf from `github.com/google/fonts/raw/main/ofl/…`.
4. Color.kt from web palette: background `#faf8f5`, text `#1c2421`, primary `#047857`, primaryDark `#065f46`.
5. Manifest: `android:supportsRtl="true"`, `MainActivity` with `CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl)` + `KunuzTheme`.
6. Verify: `./gradlew :app:assembleDebug` succeeds. Commit `feat: scaffold compose project with theme, fonts, rtl`.

### Task 2: Data export script (web repo)

**Files:** (web repo) `scripts/export-data.ts`; produces `kunuz-android/app/src/main/assets/data/treasures.json`, `chapters.json`.

```ts
// scripts/export-data.ts — run: pnpm tsx scripts/export-data.ts
import { writeFileSync, mkdirSync } from 'node:fs';
import { CHAPTERS } from '../src/data/chapters';
import { ALL_TREASURES } from '../src/data';
const out = '../kunuz-android/app/src/main/assets/data';
mkdirSync(out, { recursive: true });
writeFileSync(`${out}/treasures.json`, JSON.stringify(ALL_TREASURES, null, 2));
writeFileSync(`${out}/chapters.json`, JSON.stringify(CHAPTERS, null, 2));
console.log(`treasures: ${ALL_TREASURES.length}, chapters: ${CHAPTERS.length}`);
```

1. Write script, run it. Verify `treasures: 140, chapters: 9`. Commit both repos.

### Task 3: Models + TreasuresRepository (TDD, ports `web:src/data/data.test.ts`)

**Files:** `data/model/Models.kt`, `data/TreasuresRepository.kt`, test `TreasuresRepositoryTest.kt`.
**Interfaces:**
```kotlin
enum class ChapterId(val value: String) { DAILY_DHIKR("daily-dhikr"), PRAYERS_MOSQUES("prayers-mosques"), EXPIATION_REPENTANCE("expiation-repentance"), RELIEF_RUQYAH("relief-ruqyah"), QURAN_VIRTUES("quran-virtues"), MORALS_RELATIONS("morals-relations"), CHARITY_ONGOING("charity-ongoing"), FASTING_SEASONS("fasting-seasons"), MANNERS_SUNAN("manners-sunan") }
enum class ChapterIconName { Sun, Compass, Sparkles, ShieldCheck, BookOpen, HeartHandshake, Coins, Moon, ScrollText }
enum class TabId { ALL, CHAPTERS, FAVORITES, TASBEEH, CHECKLIST }
enum class FontSize(val label: String, val scale: Float) { NORMAL("متوسط", 1f), LARGE("كبير", 1.15f), XLARGE("كبير جداً", 1.3f) }
@Serializable data class Treasure(id: Int, title: String, chapterId: ChapterId, hadith: String, narrator: String, source: String, grade: String, explanation: String, action: String, repeatCount: Int? = null, timeContext: String? = null, tags: List<String> = emptyList(), isSpecialDailyCandidate: Boolean? = null)
@Serializable data class Chapter(id: ChapterId, name: String, shortName: String, icon: ChapterIconName, description: String, colorClasses: String)
class TreasuresRepository(context: Context) {
    val allTreasures: List<Treasure>; val chapters: List<Chapter>
    val chapterCounts: Map<ChapterId, Int>; val total: Int
    fun getTreasureById(id: Int): Treasure?
    fun getDailyTreasure(today: LocalDate = LocalDate.now()): Treasure
    fun getRandomTreasure(random: Random = Random.Default): Treasure
}
```

1. Failing tests (Robolectric): 140 treasures, ids 1..140, required fields non-blank, valid chapterId, chapterCounts sum to 140, chapters have valid icons, getDailyTreasure deterministic, getRandomTreasure member of list.
2. Run fail → implement → run pass. Commit `feat: treasure/chapter models and repository`.

### Task 4: ArabicText logic (TDD, ports `web:src/utils/arabic.test.ts`)

**Files:** `logic/ArabicText.kt`, `ArabicTextTest.kt`.
**Interfaces:** `object ArabicText { fun removeTashkeel(text: String): String; fun matchesSearch(content: String, query: String): Boolean; fun stripTashkeelForDisplay(text: String): String; fun toArabicDigits(n: Int): String }`

1. Failing tests exact port: removeTashkeel ("السَّلَامُ"→"السلام", tatweel, "إأآٱ"→"اااا", "ةى"→"هي", empty); matchesSearch (both tashkeel directions, non-match, blank query); stripTashkeelForDisplay; toArabicDigits (0→٠, 140→١٤٠, 42→٤٢).
2. Run fail → implement → run pass. Commit `feat: arabic normalization logic`.

### Task 5: Filters + ShareFormatter (TDD, ports `web:src/utils/filters.test.ts`, `share.test.ts`)

**Files:** `logic/Filters.kt`, `logic/ShareFormatter.kt` + tests.
**Interfaces:**
```kotlin
data class FilterOptions(activeTab: TabId, favorites: Set<Int>, selectedChapter: ChapterId?, selectedTag: String?, searchQuery: String)
fun filterTreasures(treasures: List<Treasure>, opts: FilterOptions): List<Treasure>
fun formatTreasureForShare(t: Treasure): String
```

1. Failing tests: favorites tab exclusion, chapter filter, tag filter, query via ArabicText.matchesSearch, share string byte-for-byte match. Implement → pass. Commit.

### Task 6: Dates + Streak logic (TDD, ports `web:src/utils/dailyTasks.test.ts`)

**Files:** `logic/Dates.kt`, `logic/Streak.kt`, `logic/StreakTest.kt`.
**Interfaces:**
```kotlin
fun localDateKey(d: LocalDate): String
fun nextStreak(lastKey: String?, currentStreak: Int, todayKey: String, yesterdayKey: String): Pair<Int, String>?
fun effectiveStreak(lastKey: String?, storedStreak: Int, todayKey: String, yesterdayKey: String): Int
fun countCompleted(tasks: Map<String, Boolean>): Int
```

1. Failing tests: zero-padded keys, countCompleted only-true, streak increment/no-op/reset scenarios, effectiveStreak today/yesterday/stale/none. Implement → pass. Commit `feat: streak and date-key logic`.

### Task 7: DataStore repositories (TDD, Robolectric)

**Files:** `data/FavoritesRepository.kt`, `data/DailyTasksRepository.kt`, `data/SettingsRepository.kt` + tests.
**Interfaces:**
```kotlin
class FavoritesRepository(context) { val favorites: Flow<Set<Int>>; suspend fun toggle(id: Int); suspend fun clear() }
class DailyTasksRepository(context, clock: Clock = Clock.systemDefaultZone()) {
  val todayTasks: Flow<Map<String, Boolean>>; val streak: Flow<Int>
  suspend fun setAll(tasks: Map<String, Boolean>)
  suspend fun recordStreakOnCompletion(taskIds: List<String>, completed: Map<String, Boolean>)
}
class SettingsRepository(context) { val fontSize: Flow<FontSize>; val showTashkeel: Flow<Boolean>; suspend fun setFontSize(v: FontSize); suspend fun setShowTashkeel(v: Boolean) }
```

1. Failing tests (Robolectric + DataStore temp file + runTest): toggle round-trip, setAll persists, recordStreakOnCompletion scenarios, effectiveStreak via injected Clock, settings defaults + set/get. Implement → pass. Commit `feat: datastore repositories`.

### Task 8: Theme, type scale, ChapterIcon, confetti, click tone

**Files:** `ui/components/Confetti.kt`, `sound/ClickTone.kt`, `ui/components/ChapterIcon.kt`, extend `ui/theme/*`.
**Interfaces:** `ConfettiController { fun fire() }` + `@Composable fun ConfettiOverlay`. `fun playClick(ctx: Context, enabled: Boolean)`. `@Composable fun ChapterIcon(icon: ChapterIconName, modifier: Modifier)`.

1. Unit test ConfettiController.fire(). Compose smoke: ConfettiOverlay, ChapterIcon renders all 9. Implement → pass. Commit.

### Task 9: KunuzViewModel + treasure list UI (TDD)

**Files:** `ui/KunuzViewModel.kt`, `ui/components/{TreasureCard,TreasureList,ChapterChips,ActiveFiltersBar,EmptyState,FavoritesHeader}.kt` + tests.
**Interfaces (ViewModel state):**
```kotlin
data class KunuzUiState(tab: TabId, selectedChapter: ChapterId?, selectedTag: String?, searchQuery: String,
    fontSize: FontSize, showTashkeel: Boolean, favorites: Set<Int>, dailyTreasure: Treasure,
    showDailyModal: Boolean, showAboutModal: Boolean, showChecklist: Boolean,
    shareTreasure: Treasure?, tasbeehTreasure: Treasure?)
class KunuzViewModel(fav, tasks, settings, treasures) : ViewModel()
```

1. Unit test (Robolectric): initial state tab=ALL; search filters; tag selects; toggleFavorite persists; cycleFontSize. Compose UI test: card renders + favorite callback. Implement → pass. Commit.

### Task 10: App shell — scaffold, navbar, tabs, dialogs hosting

**Files:** `ui/KunuzApp.kt`, `ui/components/{TopBar,HeroBanner,ChaptersGrid,AboutDialog}.kt`; modify `MainActivity.kt`.

1. Compose UI test: tab switching, search filtering, daily dialog open/close. Implement → pass. Commit.

### Task 11: TTS + share sheet

**Files:** `speech/SpeechText.kt`, `speech/ArabicTtsController.kt`, `share/AndroidSharer.kt`.

```kotlin
fun cleanForSpeech(text: String): String
class ArabicTtsController(context) { val isSpeaking: StateFlow<Boolean>; fun speak(text: String): Boolean; fun stop(); fun shutdown() }
class AndroidSharer(context) { fun shareViaChooser(text: String); fun copyToClipboard(text: String, label: String) }
```

1. Unit test cleanForSpeech. Robolectric TTS controller transitions. Implement → pass. Commit.

### Task 12: Tasbeeh screen

**Files:** `ui/components/TasbeehScreen.kt`, `logic/Tasbeeh.kt` + tests.

```kotlin
object Tasbeeh { data class Event(val sound: Boolean, val haptic: Boolean, val confetti: Boolean)
  fun onIncrement(count: Int, target: Int): Event; fun defaultTarget(treasure: Treasure?): Int }
```

1. Unit test onIncrement + defaultTarget. Compose UI test: counter increments, reset. Implement → pass. Commit.

### Task 13: Daily checklist screen

**Files:** `ui/components/ChecklistScreen.kt`, `data/model/SunnahTask.kt` + test.

```kotlin
data class SunnahTask(id: String, title: String, category: String, reward: String)
val DEFAULT_DAILY_TASKS: List<SunnahTask>
```

Task IDs: `morning-dhikr`, `evening-dhikr`, `salat-duha`, `ayat-kursi`, `istighfar-100`, `salawat-nabi`, `daily-charity`, `salat-witr`.

1. Unit test task list matches web. Compose UI test: toggle → progress updates. Implement → pass. Commit.

### Task 14: Settings polish + adaptive icon + animations

1. Font cycle + tashkeel toggle persist, tashkeel off applies stripTashkeelForDisplay, adaptive icon (emerald + gem vector), polish animations. Commit.

### Task 15: Release readiness

1. ProGuard rules for kotlinx-serialization. README. `./gradlew :app:assembleRelease`. Commit.

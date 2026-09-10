# Kunuz Android — Design Spec

## Overview

Full-parity native Android port of the Kunuz web app — a free, open-source encyclopedia of 140 authenticated Prophetic hadiths with Arabic search, daily treasure, favorites, tasbeeh counter, daily checklist with streak, text-to-speech, sharing, and RTL layout.

## Target

- Platform: Android (native)
- Location: `/media/islamux/Variety/JavaScriptProjects/kunuz-android`
- Package: `com.islamux.kunuz`
- License: GPL-3.0-or-later

## Tech Stack

- Kotlin 2.2.x
- Jetpack Compose (BOM), Material 3
- Single-Activity, state-driven tabs + dialogs (no Navigation-Compose)
- kotlinx-serialization for JSON data
- DataStore Preferences for persistence
- ViewModel + StateFlow + coroutines
- `com.composables:icons-lucide` (fallback: material-icons-extended)
- minSdk 26, targetSdk/compileSdk 36, JDK 17
- Tests: JUnit + Robolectric + Compose UI tests

## Approach

State-driven tabs mirroring `App.tsx`: one `KunuzViewModel` holds tab/chapter/query/tag/fontSize/tashkeel/modal states. Bottom `NavigationBar` for 5 tabs. Checklists and daily-treasure open as full-screen dialogs. Tasbeeh is a dedicated tab screen. JSON data assets loaded from `assets/data/`.

## Feature Mapping (Web → Android)

| Web | Android |
|---|---|
| React 19 + Vite + TS | Compose + Material 3 |
| `types.ts` | `data/model/Models.kt` (data classes, enums) |
| `data/*.ts` (140 treasures) | `assets/data/treasures.json` + `chapters.json` (one-time export script) |
| `utils/arabic.ts` | `logic/ArabicText.kt` (same regexes) |
| `utils/filters.ts`, `share.ts` | `logic/Filters.kt`, `logic/ShareFormatter.kt` |
| `utils/dailyTasks.ts` + localStorage | `logic/Streak.kt` (pure) + `DailyTasksRepository` (DataStore) |
| `utils/speech.ts` | `speech/ArabicTtsController.kt` (TextToSpeech, ar locale, rate 0.92) |
| `useFavorites` + localStorage | `FavoritesRepository` (DataStore) |
| App.tsx state | `KunuzViewModel` |
| 19 components | 1:1 composables |
| canvas-confetti | Custom Canvas composable (~80 lines) |
| WebAudio click (580 Hz) | `ToneGenerator` |
| navigator.vibrate (25ms) | `Vibrator` / `HapticFeedback` |
| Share/copy/WhatsApp/Telegram | `Intent.ACTION_SEND` chooser + `ClipboardManager` |
| lucide-react icons | `com.composables:icons-lucide` |
| Amiri/Tajawal fonts | Bundled `.ttf` in `res/font` (SIL OFL) |
| Tailwind palette | Compose Material 3 light-only color scheme |
| Forced RTL layout | `CompositionLocalProvider(LocalLayoutDirection.Rtl)` |

## Intentional Diffs

- Local date keys (not UTC) for checklist — fixes web inconsistency
- Bottom nav instead of top tab pills (Android-idiomatic)
- JSON assets instead of compiled TS objects
- Custom Canvas confetti replaces canvas-confetti dependency

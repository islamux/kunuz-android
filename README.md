# Kunuz al-Sunnah — كنوز من السنة المطهرة

Native Android port of the [Kunuz](https://github.com/KunuzApp/Kunuz) web application — a curated Arabic hadith reference app containing 140 prophetic treasures (*kanuz*), extracted and authenticated from the chains of Sheikh Mahmoud al-Masri.

## Features

- **140 authenticated treasures** organized into 10 thematic chapters
- **Full-text search** with Arabic normalization (tashkeel-insensitive matching)
- **Favorites** — mark and filter saved treasures (persisted via DataStore)
- **Chapter navigation** — browse by chapter with icon badges
- **Tasbeeh counter** — tap-to-increment with configurable targets (10/33/100/1000), haptic feedback, sound toggle, and confetti at completion
- **Daily checklist** — 8 prophetic daily practices with streak tracking and confetti on full completion
- **Tashkeel toggle** — show or hide diacritical marks for cleaner reading
- **Font size control** — cycle through Normal / Large / XLarge text
- **Share & TTS** — copy full hadith text, share via system sheet, or listen with Arabic text-to-speech
- **About dialog** — app info and attribution
- **Adaptive launcher icon** — emerald gradient background with gold gem foreground

## Build

```bash
# Debug APK
./gradlew :app:assembleDebug

# Run unit tests (144 tests, Robolectric)
./gradlew :app:testDebugUnitTest

# Release APK (signed with debug keystore)
./gradlew :app:assembleRelease
```

APK output: `app/build/outputs/apk/debug/app-debug.apk`

Requirements: JDK 17, Android SDK (compileSdk 36, minSdk 26).

## Architecture

| Layer | Tech |
|-------|------|
| UI | Jetpack Compose, Material 3, Lucide icons |
| State | MVVM — `KunuzViewModel` + `KunuzUiState` |
| Persistence | DataStore Preferences (`FavoritesRepository`, `SettingsRepository`, `DailyTasksRepository`) |
| Data | `TreasuresRepository` — in-memory cache loaded from bundled JSON assets |
| Text | `ArabicText` — tashkeel removal, search normalization, Arabic numeral conversion |
| Sound | `ClickTone` (580 Hz sine burst), Canvas confetti animation |
| TTS | `ArabicTtsController` — platform `TextToSpeech` with Arabic locale fallback |
| Serialization | kotlinx-serialization JSON |
| Testing | Robolectric 4.14 + Compose UI test |

### Project structure

```
app/src/main/java/com/islamux/kunuz/
├── data/           # Repositories + DataStore + JSON asset loaders
├── logic/          # ArabicText, Filters, Dates, Streak, ShareFormatter
├── share/          # Android system share wrapper
├── sound/          # ClickTone sine-wave player
├── speech/         # TTS controller + speech text builder
├── ui/
│   ├── components/ # TopBar, TreasureCard, ChapterGrid, Confetti, etc.
│   ├── tasbeeh/    # Tasbeeh counter screen
│   └── theme/      # Color, Typography, Theme (Tajawal font)
```

## Web parity notes

This Android app is feature-complete with the original web version. Intentional differences:

- **Date keys**: Android uses `java.time.LocalDate` (system local dates); web uses manual UTC calculation
- **Navigation**: Android uses a Material 3 bottom navigation bar; web uses a sidebar
- **Data loading**: Android loads from bundled JSON assets; web fetches from `/data/treasures.json`
- **Confetti**: Android renders via Canvas (60-particle physics); web uses `canvas-confetti`
- **Click tone**: Android uses `ToneGenerator` (580 Hz sine burst); web uses Web Audio API
- **Persistence**: Android uses DataStore Preferences; web uses `localStorage`

All hadith content, chapter organization, treasure metadata, and daily task definitions are byte-for-byte identical to the web source.

## License

See the original [Kunuz](https://github.com/KunuzApp/Kunuz) repository for licensing information.

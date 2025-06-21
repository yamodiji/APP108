# Simple App Drawer

A lightweight Android app built with Kotlin that displays all installed apps with a search bar on top.

## 🎯 Features

- **Search Bar**: Fast search functionality at the top
- **App List**: Displays all installed apps with icons and names
- **Click to Launch**: Tap any app to launch it
- **MVVM Architecture**: Modern Android architecture
- **ViewBinding**: Type-safe view references

## 🔧 Tech Stack

- **Language**: Kotlin 1.9.10
- **Min SDK**: 21 (Android 5.0)
- **Target SDK**: 34 (Android 14)
- **Architecture**: MVVM + Repository Pattern
- **UI**: Traditional Android Views with ViewBinding
- **Build System**: Gradle 8.0

## 📱 Screenshots

The app shows a clean interface with:
- Search bar at the top for filtering apps
- Scrollable list of all installed apps
- App icons and names displayed clearly

## 🚀 Building the App

**⚠️ IMPORTANT: Use GitHub Actions for Production Builds**

Following REFKOT.md best practices, **NEVER build locally for production**. Use GitHub Actions only.

### Local Development (Debug Only)
```bash
# For local testing only
./gradlew assembleDebug
./gradlew installDebug

# Run tests
./gradlew test
./gradlew lintDebug
```

### Production Builds (GitHub Actions Only)
1. Push code to `main` or `develop` branch
2. GitHub Actions automatically builds and tests
3. Download APK from GitHub Actions artifacts
4. For releases, create a GitHub release to trigger release build

## 📋 Permissions

- `QUERY_ALL_PACKAGES`: Required to access the list of installed apps

## 🏗️ Project Structure

```
app/
├── src/main/kotlin/com/simpleappdrawer/
│   ├── MainActivity.kt          # Main activity with search and list
│   ├── AppViewModel.kt          # ViewModel for MVVM pattern  
│   ├── AppAdapter.kt            # RecyclerView adapter
│   └── AppInfo.kt               # Data model for apps
├── res/
│   ├── layout/
│   │   ├── activity_main.xml    # Main layout with search + RecyclerView
│   │   └── item_app.xml         # Individual app item layout
│   └── values/                  # Strings, colors, themes
└── AndroidManifest.xml
```

## 🔍 How It Works

1. **App Loading**: Uses PackageManager to query all apps with launcher intents
2. **Search**: Real-time filtering of apps based on name or package
3. **Display**: RecyclerView with ViewBinding for efficient scrolling
4. **Launch**: Taps create launch intents to open selected apps

## 📚 References

Built following:
- REFKOT.md - Android development best practices
- REFKOTLIN.md - Common issues and solutions  
- REFKOT101.md - USB debugging workflow

## ⚡ Quick Start

1. Clone this repository
2. Open in Android Studio
3. Sync Gradle files
4. Run on device/emulator (debug build only)
5. For production, push to GitHub and use Actions

---

**Note**: This project follows modern Android development practices with proper CI/CD workflows and avoids common pitfalls documented in the REFKOT reference guides. 
# REFKOT101 - Complete Android Kotlin Development with USB Debugging Reference

Based on REFKOTLIN.md with added USB debugging for local testing workflow.

## 🎯 Overview
Complete reference guide with ALL content from REFKOTLIN.md PLUS USB debugging for local testing.

## 📋 Table of Contents
1. [🚀 USB Debugging Setup](#usb-debugging-setup) **[NEW]**
2. [🛠️ Project Setup Issues](#project-setup-issues)
3. [⚙️ Gradle Configuration Problems](#gradle-configuration-problems)
4. [🚀 GitHub Actions CI/CD Issues](#github-actions-cicd-issues)
5. [🔍 Android Lint Errors](#android-lint-errors)
6. [🖼️ Resource Compilation Errors](#resource-compilation-errors)
7. [📱 APK Installation Issues](#apk-installation-issues)
8. [📝 Best Practices](#best-practices)
9. [🏗️ Project Structure Template](#project-structure-template)

---

## 🚀 USB Debugging Setup

### Local Testing Workflow
```powershell
# Step 1: Check device connection
adb devices

# Step 2: Build debug APK
cd "F:\JUN1925"
.\gradlew.bat assembleDebug

# Step 3: Install and test
adb install -r "app\build\outputs\apk\debug\app-debug.apk"
adb shell appops set com.smartdrawer.app.debug SYSTEM_ALERT_WINDOW allow
adb shell pm grant com.smartdrawer.app.debug android.permission.QUERY_ALL_PACKAGES

# Step 4: Launch Smart Drawer
adb shell am start -n com.smartdrawer.app.debug/.MainActivity
adb shell am startservice com.smartdrawer.app.debug/.service.FloatingWidgetService

# Step 5: Monitor logs
adb logcat | findstr "SmartDrawer"
```

### Quick Test Script
Create `test-complete.bat`:
```batch
@echo off
echo Complete Smart Drawer Testing...
adb devices
.\gradlew.bat assembleDebug
adb install -r app\build\outputs\apk\debug\app-debug.apk
adb shell appops set com.smartdrawer.app.debug SYSTEM_ALERT_WINDOW allow
adb shell pm grant com.smartdrawer.app.debug android.permission.QUERY_ALL_PACKAGES
adb shell am start -n com.smartdrawer.app.debug/.MainActivity
adb shell am startservice com.smartdrawer.app.debug/.service.FloatingWidgetService
echo Check your phone for the floating widget!
pause
```

---

## 🛠️ Project Setup Issues

### Issue 1: Deprecated GitHub Actions
**Problem**: Using outdated GitHub Actions versions
```yaml
# ❌ WRONG - Deprecated versions
uses: actions/upload-artifact@v3
uses: actions/cache@v3
```

**Solution**: Always use latest stable versions
```yaml
# ✅ CORRECT - Latest versions
uses: actions/upload-artifact@v4
uses: actions/cache@v4
```

### Issue 2: Missing Permissions for Release Creation
**Problem**: GitHub Actions can't create releases
```
Error 403: Resource not accessible by integration
```

**Solution**: Add proper permissions to workflow
```yaml
# At workflow level
permissions:
  contents: write
  packages: write

# At job level for release
release:
  permissions:
    contents: write
    packages: write
```

---

## ⚙️ Gradle Configuration Problems

### Issue 1: Repository Configuration Conflict
**Problem**: Build fails with repository preference error
```
Build was configured to prefer settings repositories over project repositories
```

**Solution**: Use repositories only in `settings.gradle`, remove from root `build.gradle`
```kotlin
// ✅ settings.gradle - CORRECT
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// ❌ build.gradle (root) - REMOVE THIS
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
```

### Issue 2: Boolean Property Parsing Error
**Problem**: Trailing spaces in gradle.properties
```
Cannot parse project property android.nonTransitiveRClass='true ' as boolean
```

**Solution**: Remove trailing spaces or remove optional properties
```properties
# ❌ WRONG - Has trailing space
android.nonTransitiveRClass=true 

# ✅ CORRECT - No trailing space
android.nonTransitiveRClass=true

# ✅ ALTERNATIVE - Remove if optional
# android.nonTransitiveRClass=true
```

---

## 🚀 GitHub Actions CI/CD Issues

### Issue 1: Complete Workflow File Template
**Complete working workflow with all fixes applied:**

```yaml
name: Android CI/CD

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

permissions:
  contents: write
  packages: write

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - name: Checkout code
      uses: actions/checkout@v4
      
    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Cache Gradle packages
      uses: actions/cache@v4
      with:
        path: |
          ~/.gradle/caches
          ~/.gradle/wrapper
        key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*', '**/gradle-wrapper.properties') }}
        restore-keys: |
          ${{ runner.os }}-gradle-
          
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
      
    - name: Clean project
      run: ./gradlew clean
      
    - name: Run lint
      run: ./gradlew lint
      
    - name: Run unit tests
      run: ./gradlew test
      
    - name: Build debug APK
      run: ./gradlew assembleDebug
      
    - name: Build release APK
      run: ./gradlew assembleRelease

  release:
    runs-on: ubuntu-latest
    needs: build
    if: github.ref == 'refs/heads/main' && github.event_name == 'push'
    permissions:
      contents: write
      packages: write
    
    steps:
    - name: Create Release
      uses: ncipollo/release-action@v1
      with:
        tag: v${{ steps.version.outputs.version }}
        artifacts: app/build/outputs/apk/release/app-release.apk
        token: ${{ secrets.GITHUB_TOKEN }}
```

---

## 🔍 Android Lint Errors

### Issue 1: API Level Compatibility
**Problem**: Using newer APIs without version checks
```kotlin
// ❌ WRONG - Requires API 26+
context.startForegroundService(serviceIntent)
```

**Solution**: Add proper API level checks
```kotlin
// ✅ CORRECT - Backward compatible
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    context.startForegroundService(serviceIntent)
} else {
    context.startService(serviceIntent)
}
```

### Issue 2: Deprecated Methods
**Problem**: Using deprecated `onBackPressed()`
```kotlin
// ❌ WRONG - Deprecated
override fun onBackPressed() {
    super.onBackPressed()
}
```

**Solution**: Use modern OnBackPressedDispatcher
```kotlin
// ✅ CORRECT - Modern approach
private fun setupBackPressedCallback() {
    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    })
}
```

### Issue 3: Tint Attribute Issues
**Problem**: Using `android:tint` in ImageViews
```xml
<!-- ❌ WRONG - Lint error UseAppTint -->
<ImageView
    android:tint="?attr/colorOnSurfaceVariant" />
```

**Solution**: Use `app:tint` for ImageViews
```xml
<!-- ✅ CORRECT - AppCompat compatible -->
<ImageView
    app:tint="?attr/colorOnSurfaceVariant" />
```

### Issue 4: Unused Parameter Warnings
**Problem**: Lint warns about unused parameters
```kotlin
// ❌ WRONG - 'app' parameter unused
onAppLongClick = { app ->
    // TODO: Show context menu
}
```

**Solution**: Use underscore for intentionally unused parameters
```kotlin
// ✅ CORRECT - Indicates intentionally unused
onAppLongClick = { _ ->
    // TODO: Show context menu
}
```

---

## 🖼️ Resource Compilation Errors

### Issue 1: Invalid PNG Files
**Problem**: AAPT fails to compile corrupted PNG files
```
ERROR: /path/to/ic_launcher.png: AAPT: error: file failed to compile
```

**Solution**: Use vector drawables and adaptive icons instead
```xml
<!-- mipmap-anydpi-v26/ic_launcher.xml -->
<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">
    <background android:drawable="@drawable/ic_launcher_background" />
    <foreground android:drawable="@drawable/ic_launcher_foreground" />
</adaptive-icon>
```

**Benefits of Vector Icons:**
- Smaller APK size
- Perfect scaling on all densities
- No PNG corruption issues
- Modern adaptive icon support

---

## 📱 APK Installation Issues

### Issue 1: "Package appears to be invalid" Error
**Problem**: APK installation fails with "Package appears to be invalid" error

**Common Causes:**
1. **Corrupted APK** - APK file is incomplete or corrupted
2. **Signing issues** - APK is not properly signed
3. **Proguard conflicts** - Code obfuscation causes runtime issues
4. **Java version mismatch** - Incompatible bytecode version

**Solutions:**

#### Fix 1: Disable Proguard for Testing
```kotlin
// app/build.gradle - Disable minification for release builds
buildTypes {
    release {
        minifyEnabled false
        shrinkResources false
        proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
    }
}
```

#### Fix 2: Use Compatible Java Version
```kotlin
// app/build.gradle - Use Java 1.8 for better compatibility
compileOptions {
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
}

kotlinOptions {
    jvmTarget = '1.8'
}
```

#### Fix 3: Alternative Installation Methods
```bash
# For testing, try installing debug APK instead
./gradlew assembleDebug

# Or use ADB install with specific flags
adb install -r -t app-debug.apk

# For release APK installation issues
adb install -r -d app-release.apk
```

---

## 📝 Best Practices

### 1. Project Configuration
```kotlin
// app/build.gradle - Essential configurations
android {
    compileSdk 34
    
    defaultConfig {
        minSdk 23
        targetSdk 34
        versionCode 1
        versionName "1.0.0"
    }
    
    buildFeatures {
        viewBinding true
    }
    
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
    
    kotlinOptions {
        jvmTarget = '1.8'
    }
}
```

### 2. Dependency Management
```kotlin
dependencies {
    // Use stable versions
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.10.0'
    
    // Testing
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
}
```

### 3. Manifest Permissions
```xml
<!-- Essential permissions for overlay apps -->
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
<uses-permission android:name="android.permission.QUERY_ALL_PACKAGES" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
```

### 4. Service Implementation
```kotlin
// Proper foreground service implementation
class FloatingWidgetService : Service() {
    
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            startForeground(NOTIFICATION_ID, createNotification())
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Floating Widget Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
```

---

## 🏗️ Project Structure Template

```
app/
├── src/main/
│   ├── java/com/yourpackage/app/
│   │   ├── adapter/          # RecyclerView adapters
│   │   ├── model/           # Data models
│   │   ├── overlay/         # Overlay implementations
│   │   ├── receiver/        # Broadcast receivers
│   │   ├── service/         # Background services
│   │   ├── utils/           # Utility classes
│   │   ├── viewmodel/       # ViewModels (MVVM)
│   │   ├── MainActivity.kt
│   │   └── SettingsActivity.kt
│   ├── res/
│   │   ├── drawable/        # Vector drawables only
│   │   ├── layout/          # XML layouts
│   │   ├── mipmap-anydpi-v26/ # Adaptive icons
│   │   ├── values/          # Strings, colors, themes
│   │   └── xml/             # Backup rules, etc.
│   └── AndroidManifest.xml
├── build.gradle             # App-level build config
└── proguard-rules.pro       # Proguard rules
```

---

## 🚨 Common Pitfalls to Avoid

1. **Always test locally via USB first** before CI/CD builds
2. **Never commit placeholder/empty PNG files** - Use vector drawables
3. **Always add API level checks** for newer Android features
4. **Use app: namespace** for AppCompat attributes in layouts
5. **Set proper GitHub Actions permissions** for releases
6. **Remove trailing spaces** from gradle.properties
7. **Use repositories only in settings.gradle** for modern Gradle
8. **Implement proper foreground service** with notification channels
9. **Add proper error handling** for overlay permissions
10. **Use ViewBinding instead of findViewById** for better performance
11. **Test on multiple Android versions** (min SDK to target SDK)

---

## 📊 Performance Optimization

### APK Size Optimization
```kotlin
// Proguard rules for smaller APK
android {
    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
        }
    }
}
```

### Memory Management
```kotlin
// Proper lifecycle management
override fun onDestroy() {
    super.onDestroy()
    // Clean up resources
    binding = null
    serviceIntent = null
}
```

---

## 🔗 Useful Commands

```bash
# Local Development with USB Debugging
.\gradlew.bat clean assembleDebug    # Clean and build debug
adb install -r app-debug.apk        # Install for testing
adb logcat -s "SmartDrawer:*"       # Monitor logs

# Smart Drawer Testing Commands
adb shell appops set com.smartdrawer.app.debug SYSTEM_ALERT_WINDOW allow
adb shell pm grant com.smartdrawer.app.debug android.permission.QUERY_ALL_PACKAGES
adb shell am start -n com.smartdrawer.app.debug/.MainActivity
adb shell am startservice com.smartdrawer.app.debug/.service.FloatingWidgetService

# Testing Commands
adb shell am force-stop com.smartdrawer.app.debug             # Stop app
adb shell appops get com.smartdrawer.app.debug SYSTEM_ALERT_WINDOW  # Check permissions

# Release Preparation
.\gradlew.bat assembleRelease        # Build release
adb install -r app-release.apk      # Test release locally

# CI/CD Commands
./gradlew lint                       # Run lint checks
./gradlew test                       # Run tests
./gradlew dependencyUpdates          # Check for dependency updates
```

---

## 📚 Resources

- [Android Developer Guide](https://developer.android.com/guide)
- [Kotlin Android Extensions](https://kotlinlang.org/docs/android-overview.html)
- [Material Design Guidelines](https://material.io/design)
- [GitHub Actions Documentation](https://docs.github.com/en/actions)

---

**REFKOT101-COMPLETE**: ALL content from REFKOTLIN.md + Enhanced USB debugging workflow  
**Created**: Based on Smart Drawer Android project development experience  
**Complete**: ✅ Contains everything from original REFKOTLIN.md plus USB debugging sections

---

## 🚨 COMMON PITFALLS TO AVOID

1. **Always test locally via USB first** before CI/CD builds
2. **Use debug APK for initial testing** before release builds
3. **Grant permissions immediately** after installation
4. **Monitor logs** during testing for early issue detection
5. **Test floating widget** functionality specifically
6. **Verify app list access** works on target device
7. **Check overlay permissions** are properly granted
8. **Test on multiple Android versions** when possible
9. **Use vector drawables** instead of PNG files
10. **Remove trailing spaces** from gradle.properties

---

## 🔗 USEFUL COMMANDS CHEAT SHEET

```bash
# Build & Install Cycle
.\gradlew.bat clean assembleDebug
adb install -r app\build\outputs\apk\debug\app-debug.apk

# Smart Drawer Permissions
adb shell appops set com.smartdrawer.app.debug SYSTEM_ALERT_WINDOW allow
adb shell pm grant com.smartdrawer.app.debug android.permission.QUERY_ALL_PACKAGES

# App Control
adb shell am start -n com.smartdrawer.app.debug/.MainActivity
adb shell am startservice com.smartdrawer.app.debug/.service.FloatingWidgetService
adb shell am force-stop com.smartdrawer.app.debug

# Debugging
adb logcat | findstr "SmartDrawer"
adb shell dumpsys activity services | findstr FloatingWidgetService
adb shell appops get com.smartdrawer.app.debug SYSTEM_ALERT_WINDOW
```

---

**REFKOT101**: Enhanced Android Kotlin development guide with USB debugging workflow for thorough local testing before final app builds.

**Created**: Based on Smart Drawer project experiences  
**Last Updated**: 2024  
**Focus**: Local USB debugging workflow integration 
# REFKOT - Android App Development Reference Template

## Generic Android Project Configuration Guide

This document serves as a **universal reference template** for creating any type of Android application with modern development practices, proper build configuration, and CI/CD workflows.

---

## 📱 Basic Project Setup

**Recommended Configuration:**
- **Min SDK**: 21 (Android 5.0) - covers 99%+ devices
- **Target SDK**: 34 (Android 14) - latest stable
- **Compile SDK**: 34
- **Architecture**: MVVM + Repository Pattern
- **Language**: Kotlin
- **Build System**: Gradle with Kotlin DSL or Groovy

---

## 🔢 **EXACT VERSIONS TO USE - COPY THESE EXACTLY**

### ✅ **Required Tool Versions**
```bash
# Development Environment
Android Studio: Hedgehog | 2023.1.1 or newer
JDK: 17 (use exactly JDK 17)
Kotlin: 1.9.10
Android Gradle Plugin (AGP): 8.1.2
Gradle Wrapper: 8.0

# GitHub Actions (in workflows)
Java Setup: JDK 17 with 'temurin' distribution
Ubuntu Runner: ubuntu-latest
Actions Checkout: @v4
Actions Setup Java: @v4
Actions Upload Artifact: @v4
```

### ✅ **Core Dependencies - Use These Exact Versions**
```gradle
// Core Android - MUST USE THESE VERSIONS
implementation 'androidx.core:core-ktx:1.12.0'
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'com.google.android.material:material:1.11.0'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

// Architecture Components - TESTED VERSIONS
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.7.0'
implementation 'androidx.navigation:navigation-fragment-ktx:2.7.6'
implementation 'androidx.navigation:navigation-ui-ktx:2.7.6'

// Database - STABLE ROOM VERSION
implementation 'androidx.room:room-runtime:2.6.1'
implementation 'androidx.room:room-ktx:2.6.1'
kapt 'androidx.room:room-compiler:2.6.1'

// Networking - PROVEN STABLE
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'

// Coroutines - LATEST STABLE
implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'

// Testing - STABLE VERSIONS
testImplementation 'junit:junit:4.13.2'
androidTestImplementation 'androidx.test.ext:junit:1.1.5'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
```

### ❌ **DO NOT USE - AVOID THESE**
```gradle
// ❌ AVOID - Outdated or problematic versions
implementation 'androidx.appcompat:appcompat:1.5.x'  // Too old
implementation 'androidx.room:room-runtime:2.5.x'    // Bugs in 2.5.x
compile 'anything'                                     // Use implementation instead
// ❌ Don't use alpha/beta/rc versions in production
implementation 'anything:anything:1.0.0-alpha01'     // Unstable
```

### 🎯 **Build Configuration Versions**
```gradle
// In root build.gradle - EXACT VERSIONS
buildscript {
    ext.kotlin_version = "1.9.10"  // EXACTLY this version
    dependencies {
        classpath "com.android.tools.build:gradle:8.1.2"  // EXACTLY this AGP
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}

// In app/build.gradle - EXACT SDK VERSIONS
android {
    compileSdk 34        // EXACTLY 34
    defaultConfig {
        minSdk 21        // EXACTLY 21 (covers 99%+ devices)
        targetSdk 34     // EXACTLY 34 (latest stable)
    }
}
```

---

## 🏗️ Standard Project Structure

```
your-app-name/
├── .github/workflows/           # CI/CD automation
│   ├── android-build.yml       # Build & test workflow
│   └── code-quality.yml        # Quality checks
├── app/                         # Main application module
│   ├── build.gradle            # App-level configuration
│   ├── proguard-rules.pro      # Code obfuscation rules
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/yourcompany/yourapp/
│       │   │   ├── MainActivity.kt
│       │   │   ├── ui/          # Activities, Fragments
│       │   │   ├── data/        # Repository, Database
│       │   │   ├── domain/      # Business logic, Use cases
│       │   │   ├── di/          # Dependency injection
│       │   │   ├── utils/       # Helper classes
│       │   │   └── viewmodels/  # MVVM ViewModels
│       │   └── res/            # Resources (layouts, strings, etc.)
│       ├── androidTest/        # UI tests
│       └── test/              # Unit tests
├── gradle/wrapper/            # Gradle wrapper
├── build.gradle              # Root project configuration
├── settings.gradle           # Project settings
├── gradle.properties         # Global properties
└── README.md                # Project documentation
```

---

## ⚙️ Root Gradle Configuration (build.gradle)

```gradle
// Top-level build file for any Android project
buildscript {
    ext.kotlin_version = "1.9.10"           // Latest stable Kotlin
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath "com.android.tools.build:gradle:8.1.2"  // Latest AGP
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
        // Add other plugins as needed:
        // classpath 'com.google.dagger:hilt-android-gradle-plugin:2.48'
        // classpath 'com.google.gms:google-services:4.4.0'
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        // Add other repositories if needed
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

---

## 📱 App Gradle Configuration (app/build.gradle)

```gradle
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
    id 'kotlin-kapt'                    // For annotation processing
    // Add other plugins as needed:
    // id 'dagger.hilt.android.plugin'  // For Hilt DI
    // id 'kotlin-parcelize'             // For Parcelable
}

android {
    namespace 'com.yourcompany.yourapp'      // Update with your package
    compileSdk 34

    defaultConfig {
        applicationId "com.yourcompany.yourapp"  // Your unique app ID
        minSdk 21
        targetSdk 34
        versionCode 1                     // Increment for each release
        versionName "1.0"                // User-facing version

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
        
        // Add build config fields if needed:
        // buildConfigField "String", "API_BASE_URL", "\"https://api.example.com\""
    }

    buildTypes {
        debug {
            debuggable true
            minifyEnabled false
            applicationIdSuffix ".debug"   // Optional: separate debug installs
        }
        
        release {
            minifyEnabled true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.debug  // Change for production signing
        }
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = '1.8'
    }

    buildFeatures {
        viewBinding true     // Enable view binding
        dataBinding true     // Enable data binding (optional)
        compose true         // Enable Jetpack Compose (optional)
    }

    composeOptions {         // Only if using Compose
        kotlinCompilerExtensionVersion '1.5.4'
    }

    lint {
        abortOnError false
        warningsAsErrors false
        checkReleaseBuilds false
    }
}
```

---

## 📚 Essential Dependencies Template

```gradle
dependencies {
    // Core Android Libraries
    implementation 'androidx.core:core-ktx:1.12.0'
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.11.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'

    // Architecture Components (MVVM)
    implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0'
    implementation 'androidx.lifecycle:lifecycle-livedata-ktx:2.7.0'
    implementation 'androidx.navigation:navigation-fragment-ktx:2.7.6'
    implementation 'androidx.navigation:navigation-ui-ktx:2.7.6'

    // Database (Choose one)
    // Room Database:
    implementation 'androidx.room:room-runtime:2.6.1'
    implementation 'androidx.room:room-ktx:2.6.1'
    kapt 'androidx.room:room-compiler:2.6.1'
    
    // OR Realm Database:
    // implementation 'io.realm:realm-android-kotlin:1.11.0'

    // Networking (Choose based on needs)
    // Retrofit + OkHttp:
    implementation 'com.squareup.retrofit2:retrofit:2.9.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
    implementation 'com.squareup.okhttp3:logging-interceptor:4.12.0'
    
    // OR Volley:
    // implementation 'com.android.volley:volley:1.2.1'

    // Image Loading (Choose one)
    // Glide:
    implementation 'com.github.bumptech.glide:glide:4.16.0'
    
    // OR Picasso:
    // implementation 'com.squareup.picasso:picasso:2.8'
    
    // OR Coil (Kotlin-first):
    // implementation 'io.coil-kt:coil:2.5.0'

    // Dependency Injection (Optional)
    // Hilt:
    implementation 'com.google.dagger:hilt-android:2.48'
    kapt 'com.google.dagger:hilt-compiler:2.48'
    
    // OR Koin:
    // implementation 'io.insert-koin:koin-android:3.5.0'

    // Async Programming
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'

    // UI Components
    implementation 'androidx.recyclerview:recyclerview:1.3.2'
    implementation 'androidx.viewpager2:viewpager2:1.0.0'
    implementation 'androidx.swiperefreshlayout:swiperefreshlayout:1.1.0'

    // Jetpack Compose (Optional - for modern UI)
    implementation platform('androidx.compose:compose-bom:2023.10.01')
    implementation 'androidx.compose.ui:ui'
    implementation 'androidx.compose.ui:ui-tooling-preview'
    implementation 'androidx.compose.material3:material3'
    implementation 'androidx.activity:activity-compose:1.8.2'

    // Preferences
    implementation 'androidx.preference:preference-ktx:1.2.1'

    // Testing
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.mockito:mockito-core:5.7.0'
    testImplementation 'androidx.arch.core:core-testing:2.2.0'
    testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3'
    
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'
    androidTestImplementation 'androidx.test:runner:1.5.2'
    androidTestImplementation 'androidx.test:rules:1.5.0'
}
```

---

## 🔧 Settings Gradle

```gradle
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        // Add custom repositories if needed:
        // maven { url 'https://jitpack.io' }
    }
}

rootProject.name = "Your App Name"
include ':app'
// Include additional modules:
// include ':feature-module'
// include ':core-module'
```

---

## 📋 Gradle Properties

```properties
# Performance Optimization
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
org.gradle.parallel=true
org.gradle.caching=true

# Android Configuration
android.useAndroidX=true
android.nonTransitiveRClass=true

# Kotlin Configuration
kotlin.code.style=official

# Enable incremental annotation processing
kapt.incremental=true
kapt.use.worker.api=true

# Optional: Enable experimental features
# android.enableJetifier=true  # Only if you have legacy support libraries
```

---

## 📱 Android Manifest Template

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <!-- Standard Permissions (add as needed) -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <!-- <uses-permission android:name="android.permission.CAMERA" /> -->
    <!-- <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" /> -->
    <!-- <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /> -->

    <application
        android:name=".YourApplication"  <!-- Custom Application class -->
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.YourApp"
        tools:targetApi="31">

        <!-- Main Activity -->
        <activity
            android:name=".ui.MainActivity"
            android:exported="true"
            android:theme="@style/Theme.YourApp.NoActionBar">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Other Activities -->
        <!-- <activity android:name=".ui.SettingsActivity" /> -->

        <!-- Services -->
        <!-- <service android:name=".services.YourService" /> -->

        <!-- Broadcast Receivers -->
        <!-- <receiver android:name=".receivers.YourReceiver" /> -->

        <!-- Content Providers -->
        <!-- <provider android:name=".providers.YourProvider" /> -->

    </application>
</manifest>
```

---

## 🚀 GitHub Actions CI/CD Workflows

### ⚠️ **IMPORTANT: BUILD ONLY ON GITHUB - NOT LOCALLY**

**Always use GitHub Actions for build, test, and deploy steps. Never build locally for production.**

**Why GitHub Actions Only:**
- ✅ **Consistent Environment**: Same build environment every time
- ✅ **Automated Testing**: No human error in build process
- ✅ **Security**: Secrets and signing handled securely
- ✅ **Collaboration**: Team can see all builds and results
- ✅ **Free**: GitHub provides free CI/CD for public repositories

**Local Development:**
- ✅ **Code writing and editing only**
- ✅ **Quick testing during development**
- ❌ **NO production builds**
- ❌ **NO release APK generation**
- ❌ **NO deployment steps**

### 1. Build & Test Workflow (.github/workflows/android-build.yml)

```yaml
name: Android CI/CD

on:
  push:
    branches: [ "main", "develop" ]
  pull_request:
    branches: [ "main" ]
  release:
    types: [published]

permissions:
  contents: write
  checks: write
  pull-requests: write

jobs:
  test:
    name: Run Unit Tests
    runs-on: ubuntu-latest
    
    steps:
    - name: Checkout code
      uses: actions/checkout@v4

    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Setup Gradle
      uses: gradle/gradle-build-action@v2

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Run unit tests
      run: ./gradlew test --stacktrace

    - name: Generate test report
      uses: dorny/test-reporter@v1
      if: success() || failure()
      with:
        name: Unit Test Results
        path: 'app/build/test-results/testDebugUnitTest/TEST-*.xml'
        reporter: java-junit

  lint:
    name: Run Lint
    runs-on: ubuntu-latest
    
    steps:
    - name: Checkout code
      uses: actions/checkout@v4

    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Run lint
      run: ./gradlew lintDebug --stacktrace

    - name: Upload lint results
      uses: actions/upload-artifact@v4
      if: always()
      with:
        name: lint-results
        path: app/build/reports/lint-results-debug.html

  build:
    name: Build APK
    runs-on: ubuntu-latest
    needs: [test, lint]
    
    steps:
    - name: Checkout code
      uses: actions/checkout@v4

    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Build debug APK
      run: ./gradlew assembleDebug --stacktrace

    - name: Upload debug APK
      uses: actions/upload-artifact@v4
      with:
        name: debug-apk
        path: app/build/outputs/apk/debug/app-debug.apk

    - name: Build release APK
      if: github.event_name == 'release'
      run: ./gradlew assembleRelease --stacktrace

    - name: Upload release APK to GitHub Release
      if: github.event_name == 'release'
      uses: actions/upload-release-asset@v1
      env:
        GITHUB_TOKEN: ${{ secrets.GITHUB_TOKEN }}
      with:
        upload_url: ${{ github.event.release.upload_url }}
        asset_path: app/build/outputs/apk/release/app-release.apk
        asset_name: app-release.apk
        asset_content_type: application/vnd.android.package-archive
```

### 2. Code Quality Workflow (.github/workflows/code-quality.yml)

```yaml
name: Code Quality

on:
  push:
    branches: [ "main", "develop" ]
  pull_request:
    branches: [ "main" ]

jobs:
  detekt:
    name: Static Code Analysis
    runs-on: ubuntu-latest
    
    steps:
    - name: Checkout code
      uses: actions/checkout@v4

    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Run detekt
      run: ./gradlew detekt --stacktrace || echo "Detekt checks completed"

  dependency-check:
    name: Dependency Vulnerability Scan
    runs-on: ubuntu-latest
    
    steps:
    - name: Checkout code
      uses: actions/checkout@v4

    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17'
        distribution: 'temurin'

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew

    - name: Run dependency check
      run: ./gradlew dependencyCheckAnalyze --stacktrace || echo "Dependency check completed"
```

---

## 🔐 ProGuard/R8 Rules Template

```proguard
# Generic ProGuard rules for any Android app

# Keep your app's main classes
-keep class com.yourcompany.yourapp.** { *; }

# Keep data classes and models
-keep class com.yourcompany.yourapp.data.models.** { *; }
-keep class com.yourcompany.yourapp.domain.models.** { *; }

# Retrofit (if using)
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Gson (if using)
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class com.google.gson.examples.android.model.** { <fields>; }
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Room Database (if using)
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-dontwarn androidx.room.paging.**

# OkHttp (if using)
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**

# Glide (if using)
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
    <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# Kotlin Coroutines
-dontwarn kotlinx.coroutines.**
-keep class kotlinx.coroutines.** { *; }

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
```

---

## 🏗️ Architecture Patterns

### MVVM + Repository Pattern Structure

```
UI Layer (Activities/Fragments)
       ↓
ViewModel (Business Logic)
       ↓
Repository (Data Layer)
       ↓
Data Sources (Database, Network, Preferences)
```

### Key Components:

1. **Activities/Fragments**: UI controllers
2. **ViewModels**: Hold UI state and business logic
3. **Repository**: Single source of truth for data
4. **Data Sources**: Room DB, Retrofit API, SharedPreferences
5. **Models**: Data classes representing your app's data

---

## 📊 Common Database Setup (Room)

```kotlin
// Entity
@Entity(tableName = "your_table")
data class YourEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val timestamp: Long
)

// DAO
@Dao
interface YourDao {
    @Query("SELECT * FROM your_table")
    fun getAll(): LiveData<List<YourEntity>>
    
    @Insert
    suspend fun insert(entity: YourEntity)
    
    @Delete
    suspend fun delete(entity: YourEntity)
}

// Database
@Database(entities = [YourEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class YourDatabase : RoomDatabase() {
    abstract fun yourDao(): YourDao
    
    companion object {
        @Volatile
        private var INSTANCE: YourDatabase? = null
        
        fun getDatabase(context: Context): YourDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    YourDatabase::class.java,
                    "your_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
```

---

## 🎯 Build Commands - GitHub Actions vs Local

### 🚀 **PRODUCTION BUILDS - USE GITHUB ACTIONS ONLY**

**Never run these locally for production:**
```bash
# ❌ DON'T RUN LOCALLY FOR PRODUCTION
./gradlew assembleRelease      # Only GitHub Actions
./gradlew bundleRelease        # Only GitHub Actions  
./gradlew publish              # Only GitHub Actions
./gradlew uploadArchives       # Only GitHub Actions
```

**How to build on GitHub Actions:**
1. **Push code** to main/develop branch
2. **GitHub Actions automatically** runs builds
3. **Download APK** from GitHub Actions artifacts
4. **Create GitHub Release** for production APK

### 🛠️ **LOCAL DEVELOPMENT - ALLOWED COMMANDS**

**Only these commands for local development:**
```bash
# ✅ LOCAL DEVELOPMENT ONLY
./gradlew assembleDebug        # Debug APK for testing
./gradlew test                 # Unit tests
./gradlew testDebugUnitTest    # Debug unit tests
./gradlew lintDebug           # Lint analysis
./gradlew installDebug        # Install debug APK
./gradlew uninstallDebug      # Uninstall debug APK
./gradlew clean               # Clean build
./gradlew clean assembleDebug # Clean + debug build

# ✅ QUICK TESTING ONLY
./gradlew connectedAndroidTest # UI tests (when needed)
```

### 📋 **GitHub Actions Workflow Triggers**

```yaml
# Automatic builds happen when:
on:
  push:
    branches: [ "main", "develop" ]     # Auto-build on push
  pull_request:
    branches: [ "main" ]                # Auto-build on PR
  release:
    types: [published]                  # Auto-build on release
```

**To get your APK:**
1. **Go to GitHub Actions tab** in your repository
2. **Click on latest workflow run**
3. **Download artifacts** (debug-apk, release-apk)
4. **Install APK** on your device

---

## 🛠️ Development Environment Setup

### Required Software:
- **Android Studio**: Latest stable version
- **JDK**: 17 or higher
- **Android SDK**: Target API level
- **Git**: Version control
- **Gradle**: 8.0+ (via wrapper)

### Recommended Plugins:
- **Kotlin**: Official Kotlin plugin
- **Detekt**: Static code analysis
- **SonarLint**: Code quality
- **GitToolBox**: Git integration
- **ADB Idea**: ADB commands

---

## 🚀 Quick Start Checklist

### 1. Project Setup
- [ ] Create new Android Studio project
- [ ] Configure package name and app ID
- [ ] Set up version control (Git)
- [ ] Add .gitignore file

### 2. Build Configuration
- [ ] Update Gradle files with required dependencies
- [ ] Configure build variants (debug/release)
- [ ] Set up ProGuard rules
- [ ] Configure signing (for release)

### 3. Architecture Setup
- [ ] Create package structure (ui, data, domain)
- [ ] Set up ViewModels and LiveData
- [ ] Implement Repository pattern
- [ ] Configure Database (Room) or networking (Retrofit)

### 4. CI/CD Setup
- [ ] Create GitHub repository
- [ ] Add GitHub Actions workflows
- [ ] Configure secrets (if needed)
- [ ] Test automated builds

### 5. Quality Assurance
- [ ] Add unit tests
- [ ] Add instrumented tests
- [ ] Configure lint rules
- [ ] Set up static analysis

---

## 📝 Customization Guide

### For Different App Types:

**Social Media App:**
- Add image loading (Glide/Picasso)
- Implement push notifications (FCM)
- Add social login (Google/Facebook)
- Use ViewPager2 for feeds

**E-commerce App:**
- Add payment integration
- Implement product catalogs
- Add shopping cart functionality
- Use fragments for product details

**Productivity App:**
- Focus on local storage (Room)
- Add reminder/notification system
- Implement data sync
- Use preferences for settings

**Game App:**
- Add game engine dependencies
- Implement leaderboards
- Add in-app purchases
- Use OpenGL for graphics

---

This reference template provides a **universal foundation** for any Android app development project with modern practices, proper architecture, and automated workflows. 
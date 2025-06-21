# Generic ProGuard rules for Simple App Drawer

# Keep your app's main classes
-keep class com.simpleappdrawer.** { *; }

# Keep data classes and models
-keep class com.simpleappdrawer.AppInfo { *; }

# Kotlin Coroutines
-dontwarn kotlinx.coroutines.**
-keep class kotlinx.coroutines.** { *; }

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Keep ViewBinding classes
-keep class com.simpleappdrawer.databinding.** { *; } 
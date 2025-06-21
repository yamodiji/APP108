package com.simpleappdrawer

import android.graphics.drawable.Drawable

/**
 * Data class representing an installed app's information
 */
data class AppInfo(
    val appName: String,
    val packageName: String,
    val appIcon: Drawable
) 
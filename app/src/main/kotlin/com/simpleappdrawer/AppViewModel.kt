package com.simpleappdrawer

import android.content.Intent
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel for managing app data following MVVM pattern
 */
class AppViewModel : ViewModel() {
    
    private val _allApps = MutableLiveData<List<AppInfo>>()
    private val _filteredApps = MutableLiveData<List<AppInfo>>()
    val filteredApps: LiveData<List<AppInfo>> = _filteredApps
    
    private var currentSearchQuery = ""
    
    /**
     * Load all installed apps asynchronously
     */
    fun loadInstalledApps(packageManager: PackageManager) {
        viewModelScope.launch {
            val apps = withContext(Dispatchers.IO) {
                loadAppsFromPackageManager(packageManager)
            }
            _allApps.value = apps
            _filteredApps.value = apps
        }
    }
    
    /**
     * Search and filter apps based on query
     */
    fun searchApps(query: String) {
        currentSearchQuery = query
        val allApps = _allApps.value ?: emptyList()
        
        if (query.isBlank()) {
            _filteredApps.value = allApps
        } else {
            val filtered = allApps.filter { app ->
                app.appName.contains(query, ignoreCase = true) ||
                app.packageName.contains(query, ignoreCase = true)
            }
            _filteredApps.value = filtered
        }
    }
    
    /**
     * Load apps from PackageManager - runs on IO thread
     */
    private fun loadAppsFromPackageManager(packageManager: PackageManager): List<AppInfo> {
        val apps = mutableListOf<AppInfo>()
        
        try {
            // Get all apps with launcher intent
            val intent = Intent(Intent.ACTION_MAIN, null).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            
            val resolveInfoList = packageManager.queryIntentActivities(intent, 0)
            
            for (resolveInfo in resolveInfoList) {
                val activityInfo = resolveInfo.activityInfo
                val packageName = activityInfo.packageName
                val appName = resolveInfo.loadLabel(packageManager).toString()
                val appIcon = resolveInfo.loadIcon(packageManager)
                
                apps.add(AppInfo(appName, packageName, appIcon))
            }
            
            // Sort apps alphabetically by name
            apps.sortBy { it.appName.lowercase() }
            
        } catch (e: Exception) {
            // Handle any exceptions during app loading
            e.printStackTrace()
        }
        
        return apps
    }
} 
package com.simpleappdrawer

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.simpleappdrawer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var appAdapter: AppAdapter
    private lateinit var viewModel: AppViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[AppViewModel::class.java]
        
        setupRecyclerView()
        setupSearchBar()
        observeViewModel()
        
        // Load apps
        viewModel.loadInstalledApps(packageManager)
    }
    
    private fun setupRecyclerView() {
        appAdapter = AppAdapter()
        binding.appsRecyclerView.apply {
            adapter = appAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
    }
    
    private fun setupSearchBar() {
        binding.searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchApps(s.toString())
            }
            
            override fun afterTextChanged(s: Editable?) {}
        })
    }
    
    private fun observeViewModel() {
        viewModel.filteredApps.observe(this) { apps ->
            appAdapter.submitList(apps)
            
            // Show/hide no apps message
            if (apps.isEmpty()) {
                binding.noAppsTextView.visibility = View.VISIBLE
                binding.appsRecyclerView.visibility = View.GONE
            } else {
                binding.noAppsTextView.visibility = View.GONE
                binding.appsRecyclerView.visibility = View.VISIBLE
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Clear binding reference to avoid memory leaks
        // Note: ViewBinding automatically handles this, but good practice
    }
} 
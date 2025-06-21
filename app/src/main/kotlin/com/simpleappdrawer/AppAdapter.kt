package com.simpleappdrawer

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.simpleappdrawer.databinding.ItemAppBinding

/**
 * RecyclerView adapter for displaying apps using ViewBinding
 */
class AppAdapter : ListAdapter<AppInfo, AppAdapter.AppViewHolder>(AppDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemAppBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class AppViewHolder(private val binding: ItemAppBinding) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(appInfo: AppInfo) {
            binding.appNameTextView.text = appInfo.appName
            binding.appIconImageView.setImageDrawable(appInfo.appIcon)
            
            // Handle app launch on click
            binding.root.setOnClickListener {
                val context = binding.root.context
                val launchIntent = context.packageManager.getLaunchIntentForPackage(appInfo.packageName)
                launchIntent?.let {
                    context.startActivity(it)
                }
            }
        }
    }

    /**
     * DiffUtil callback for efficient list updates
     */
    class AppDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem.packageName == newItem.packageName
        }

        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
            return oldItem == newItem
        }
    }
} 
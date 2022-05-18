package com.krivochkov.homework_2.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.ActivityMainBinding
import com.krivochkov.homework_2.presentation.chat.FilePickSharedViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val filePickSharedViewModel: FilePickSharedViewModel by viewModels()

    private val filePicker = FilePicker(activityResultRegistry, this) {
        filePickSharedViewModel.sendFileUri(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)

        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_channel_chat,
                R.id.navigation_create_channel,
                R.id.navigation_topic_pick,
                R.id.navigation_topic_chat -> {
                    window.statusBarColor = getColor(R.color.teal)
                    hideBottomNav()
                }
                else -> {
                    window.statusBarColor = getColor(R.color.black_100)
                    showBottomNav()
                }
            }
        }

        filePickSharedViewModel.pickFileEvent.observe(this) {
            it.getContentIfNotHandled()?.let {
                filePicker.pickFile()
            }
        }
    }

    private fun hideBottomNav() {
        binding.navView.isVisible = false
    }

    private fun showBottomNav() {
        binding.navView.isVisible = true
    }
}
package com.krivochkov.homework_2.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.ActivityMainBinding
import com.krivochkov.homework_2.presentation.message.MessageSharedViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val messageSharedViewModel: MessageSharedViewModel by viewModels()

    private val filePicker = FilePicker(activityResultRegistry, this) {
        messageSharedViewModel.sendFileUri(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)

        binding.navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_messages -> {
                    window.statusBarColor = getColor(R.color.teal)
                    hideBottomNav()
                }
                else -> {
                    window.statusBarColor = getColor(R.color.black_100)
                    showBottomNav()
                }
            }
        }

        messageSharedViewModel.pickFileEvent.observe(this) {
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
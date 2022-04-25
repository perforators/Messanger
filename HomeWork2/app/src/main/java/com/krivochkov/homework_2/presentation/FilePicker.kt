package com.krivochkov.homework_2.presentation

import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner

class FilePicker(
    activityResultRegistry: ActivityResultRegistry,
    lifecycleOwner: LifecycleOwner,
    onPick: (fileUri: Uri?) -> Unit
) {

    private val filePickerLauncher: ActivityResultLauncher<String> = activityResultRegistry
            .register(REGISTRY_KEY, lifecycleOwner, ActivityResultContracts.GetContent(), onPick)

    fun pickFile() {
        filePickerLauncher.launch(MIME_TYPE)
    }

    companion object {

        private const val MIME_TYPE = "*/*"
        private const val REGISTRY_KEY = "Get file key"
    }
}
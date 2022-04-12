package com.krivochkov.homework_2.presentation.message

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.presentation.SingleEvent

class MessageSharedViewModel : ViewModel() {

    private val _pickFileEvent: MutableLiveData<SingleEvent<Any>> = MutableLiveData()
    val pickFileEvent: LiveData<SingleEvent<Any>>
        get() = _pickFileEvent

    private val _fileUri: MutableLiveData<SingleEvent<Uri?>> = MutableLiveData()
    val fileUri: LiveData<SingleEvent<Uri?>>
        get() = _fileUri

    fun pickFile() {
        _pickFileEvent.value = SingleEvent(Any())
    }

    fun sendFileUri(uri: Uri?) {
        _fileUri.value = SingleEvent(uri)
    }
}
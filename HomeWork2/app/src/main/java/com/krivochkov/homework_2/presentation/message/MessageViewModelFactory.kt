package com.krivochkov.homework_2.presentation.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MessageViewModelFactory(
    private val channelName: String,
    private val topicName: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MessageViewModel(channelName, topicName) as T
    }
}
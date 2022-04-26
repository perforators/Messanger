package com.krivochkov.homework_2.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.krivochkov.homework_2.presentation.chat.elm.ChatStoreFactory

class ChatViewModelFactory(
    private val chatStoreFactory: ChatStoreFactory
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatViewModel(chatStoreFactory) as T
    }
}
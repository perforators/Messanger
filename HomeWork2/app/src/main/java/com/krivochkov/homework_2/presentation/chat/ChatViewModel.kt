package com.krivochkov.homework_2.presentation.chat

import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.presentation.chat.elm.ChatStoreFactory

class ChatViewModel(chatStoreFactory: ChatStoreFactory) : ViewModel() {

    val chatStore = chatStoreFactory.provide()
}
package com.krivochkov.homework_2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.message_repository.MessageRepository
import com.krivochkov.homework_2.message_repository.MessageRepositoryImpl
import com.krivochkov.homework_2.models.Message

class MessagesViewModel(
    private val repository: MessageRepository = MessageRepositoryImpl()
) : ViewModel() {

    private val _messages: MutableLiveData<List<Message>> = MutableLiveData()
    val messages: LiveData<List<Message>>
        get() = _messages

    fun loadMessages() {
        _messages.value = repository.loadAllMessages()
    }

    fun updateReaction(messageId: Long, emoji: String) {
        repository.updateReaction(messageId, emoji)
    }

    fun sendMessage(message: String) {
        repository.sendMessage(message)
    }
}
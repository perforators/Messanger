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

    private var isLastActionSending = false

    private val _messages: MutableLiveData<MessageResult> = MutableLiveData()
    val messages: LiveData<MessageResult>
        get() = _messages

    init {
        refreshMessages()
    }

    fun refreshMessages() {
        _messages.value = MessageResult(repository.getAllMessages(), isLastActionSending)
    }

    fun updateReaction(messageId: Long, emoji: String) {
        repository.updateReaction(messageId, emoji)
        isLastActionSending = false
    }

    fun sendMessage(message: String) {
        repository.sendMessage(message)
        isLastActionSending = true
    }

    data class MessageResult(val messages: List<Message>, val isLastActionSending: Boolean)
}
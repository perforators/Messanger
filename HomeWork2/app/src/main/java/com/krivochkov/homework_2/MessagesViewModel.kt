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

    private val _event: MutableLiveData<MessageEvent> = MutableLiveData()
    val event: LiveData<MessageEvent>
        get() = _event

    init {
        refreshMessages()
    }

    private fun refreshMessages() {
        _event.value = MessageEvent.MessagesRefreshed(repository.getAllMessages())
    }

    fun updateReaction(messageId: Long, emoji: String) {
        repository.updateReaction(messageId, emoji)
        _event.value = MessageEvent.ReactionUpdated(repository.getAllMessages())
    }

    fun sendMessage(message: String) {
        repository.sendMessage(message)
        _event.value = MessageEvent.MessageSent(repository.getAllMessages())
    }

    sealed class MessageEvent(val messages: List<Message>) {
        class MessagesRefreshed(messages: List<Message>): MessageEvent(messages)
        class MessageSent(messages: List<Message>): MessageEvent(messages)
        class ReactionUpdated(messages: List<Message>): MessageEvent(messages)
    }
}
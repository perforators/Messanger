package com.krivochkov.homework_2.presentation.message

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.data.repositories.MessageRepositoryImpl
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.domain.use_cases.message.GetAllMessagesUseCase
import com.krivochkov.homework_2.domain.use_cases.message.SendMessageUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.UpdateReactionUseCase

class MessageViewModel : ViewModel() {

    private val getAllMessagesUseCase: GetAllMessagesUseCase
    private val updateReactionUseCase: UpdateReactionUseCase
    private val sendMessageUseCase: SendMessageUseCase

    private val _messages: MutableLiveData<List<Message>> = MutableLiveData()
    val messages: LiveData<List<Message>>
        get() = _messages

    init {
        val repository = MessageRepositoryImpl()
        getAllMessagesUseCase = GetAllMessagesUseCase(repository)
        updateReactionUseCase = UpdateReactionUseCase(repository)
        sendMessageUseCase = SendMessageUseCase(repository)

        loadMessages()
    }

    fun loadMessages() {
        _messages.value = getAllMessagesUseCase()
    }

    fun updateReaction(messageId: Long, emoji: String) {
        updateReactionUseCase(messageId, emoji)
    }

    fun sendMessage(message: String) {
        sendMessageUseCase(message)
    }
}
package com.krivochkov.homework_2.message_repository

import com.krivochkov.homework_2.models.Message

interface MessageRepository {

    fun loadAllMessages(): List<Message>

    fun sendMessage(content: String)

    fun updateReaction(messageId: Long, emoji: String)
}
package com.krivochkov.homework_2.domain.repositories

import com.krivochkov.homework_2.domain.models.Message

interface MessageRepository {

    fun getAllMessages(): List<Message>

    fun sendMessage(content: String)

    fun updateReaction(messageId: Long, emoji: String)
}
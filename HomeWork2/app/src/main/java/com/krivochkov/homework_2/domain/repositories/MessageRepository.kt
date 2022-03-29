package com.krivochkov.homework_2.domain.repositories

import com.krivochkov.homework_2.domain.models.Message
import io.reactivex.Completable
import io.reactivex.Single

interface MessageRepository {

    fun getAllMessages(): Single<List<Message>>

    fun sendMessage(content: String): Completable

    fun updateReaction(messageId: Long, emoji: String): Completable
}
package com.krivochkov.homework_2.domain.repositories

import com.krivochkov.homework_2.domain.models.Message
import io.reactivex.Completable
import io.reactivex.Single

interface MessageRepository {

    fun getMessages(
        channelName: String,
        topicName: String,
        lastMessageId: Long,
        numBefore: Int
    ): Single<List<Message>>

    fun getSingleMessage(messageId: Long): Single<Message>

    fun getCachedMessages(channelName: String, topicName: String = ""): Single<List<Message>>

    fun sendMessage(channelName: String, topicName: String, content: String): Completable

    fun removeReaction(messageId: Long, emojiName: String): Completable

    fun addReaction(messageId: Long, emojiName: String): Completable
}
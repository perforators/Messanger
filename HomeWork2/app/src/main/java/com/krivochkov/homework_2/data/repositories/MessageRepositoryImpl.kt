package com.krivochkov.homework_2.data.repositories

import android.util.Log
import com.krivochkov.homework_2.data.mappers.mapToMessage
import com.krivochkov.homework_2.data.mappers.mapToMessageEntity
import com.krivochkov.homework_2.data.sources.local.data_sources.MessageLocalDataSource
import com.krivochkov.homework_2.data.sources.local.entity.MessageEntity
import com.krivochkov.homework_2.data.sources.remote.data_sources.MessageRemoteDataSource
import com.krivochkov.homework_2.data.sources.remote.dto.NarrowDto
import com.krivochkov.homework_2.data.sources.remote.request.*
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.domain.repositories.MessageRepository
import com.krivochkov.homework_2.domain.repositories.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageRemoteDataSource: MessageRemoteDataSource,
    private val messageLocalDataSource: MessageLocalDataSource,
    private val userRepository: UserRepository
) : MessageRepository {

    override fun getMessages(
        channelName: String,
        topicName: String,
        lastMessageId: Long,
        numBefore: Int
    ): Single<List<Message>> {
        val narrows = mutableListOf(NarrowDto(NarrowDto.OPERATOR_CHANNEL, channelName))
        if (topicName.isNotEmpty()) narrows.add(NarrowDto(NarrowDto.OPERATOR_TOPIC, topicName))

        val anchor = if (lastMessageId <= 0) DEFAULT_ANCHOR else lastMessageId - 1

        val request = Request.Builder()
            .addQuery(ANCHOR_KEY, anchor.toString())
            .addQuery(NUM_BEFORE_KEY, numBefore.toString())
            .addQuery(NUM_AFTER_KEY, DEFAULT_NUM_AFTER)
            .addQuery(NARROW_KEY, Json.encodeToString(narrows))
            .build()

        return messageRemoteDataSource.getMessages(request)
            .flatMap { messagesDto ->
                userRepository.getMyUser()
                    .map { myUser ->
                        messagesDto.map { messageDto ->
                            messageDto.mapToMessage(myUser.id)
                        }
                    }
                    .doOnSuccess { cacheMessages(channelName, it) }
            }
    }

    override fun getSingleMessage(messageId: Long): Single<Message> {
        return messageRemoteDataSource.getSingleMessage(messageId)
            .flatMap { messageDto ->
                userRepository.getMyUser()
                    .map { myUser ->
                        messageDto.mapToMessage(myUser.id)
                    }
            }
    }

    override fun sendMessage(channelName: String, topicName: String, content: String): Completable {
        val request = Request.Builder()
            .addQuery(TYPE_KEY, DEFAULT_TYPE)
            .addQuery(TO_KEY, channelName)
            .addQuery(TOPIC_KEY, topicName)
            .addQuery(CONTENT_KEY, content)
            .build()

        return messageRemoteDataSource.sendMessage(request)
    }

    override fun removeReaction(messageId: Long, emojiName: String): Completable {
        return messageRemoteDataSource.removeReaction(messageId, emojiName)
    }

    override fun addReaction(messageId: Long, emojiName: String): Completable {
        return messageRemoteDataSource.addReaction(messageId, emojiName)
    }

    override fun getCachedMessages(channelName: String, topicName: String): Single<List<Message>> {
        return if (topicName.isNotEmpty()) {
            messageLocalDataSource.getMessagesFromTopic(channelName, topicName)
        } else {
            messageLocalDataSource.getMessagesFromChannel(channelName)
        }.map { it.map { messageEntity -> messageEntity.mapToMessage() } }
    }

    private fun cacheMessages(channelName: String, messages: List<Message>) {
        val groups = messages.groupBy { it.topic }
        groups.forEach { cacheMessagesByTopic(channelName, it.key, it.value) }
    }

    private fun cacheMessagesByTopic(
        channelName: String,
        topicName: String,
        messages: List<Message>
    ) {
        messageLocalDataSource.getMessagesFromTopic(channelName, topicName)
            .map { cachedMessages ->
                val newMessages = messages.map { it.mapToMessageEntity(channelName, topicName) }
                messageLocalDataSource.refreshMessages(
                    channelName,
                    topicName,
                    getUpdatedListMessages(cachedMessages, newMessages)
                )
            }
            .subscribeOn(Schedulers.io())
            .subscribeBy(onError = { Log.d(TAG, it.printStackTrace().toString()) })
    }

    private fun getUpdatedListMessages(
        cachedMessages: List<MessageEntity>,
        newMessages: List<MessageEntity>
    ): List<MessageEntity> {
        if (newMessages.isEmpty()) return cachedMessages
        if (cachedMessages.isEmpty()) return newMessages

        val resultMessages = mutableListOf<MessageEntity>().apply {
            addAll(newMessages)
            cachedMessages.forEach { message ->
                if (find { message.id == it.id } == null) add(message)
            }
            sortBy { it.date }
        }

        return if (resultMessages.size > MAX_COUNT_CACHED_MESSAGES)
            resultMessages.subList(
                resultMessages.size - MAX_COUNT_CACHED_MESSAGES,
                resultMessages.size
            )
        else
            resultMessages
    }

    companion object {

        private const val TAG = "MessageRepositoryImpl"
        private const val MAX_COUNT_CACHED_MESSAGES = 50
        private const val DEFAULT_TYPE = "stream"
        private const val DEFAULT_ANCHOR = "newest"
        private const val DEFAULT_NUM_AFTER = "0"
    }
}
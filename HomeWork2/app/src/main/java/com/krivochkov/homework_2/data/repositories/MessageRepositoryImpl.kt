package com.krivochkov.homework_2.data.repositories

import com.krivochkov.homework_2.data.sources.remote.dto.NarrowDto
import com.krivochkov.homework_2.data.sources.remote.data_sources.MessageRemoteDataSourceImpl
import com.krivochkov.homework_2.data.sources.remote.data_sources.MessageRemoteDataSource
import com.krivochkov.homework_2.data.sources.remote.request.*
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.domain.repositories.MessageRepository
import com.krivochkov.homework_2.domain.repositories.UserRepository
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MessageRepositoryImpl(
    private val messageRemoteDataSource: MessageRemoteDataSource = MessageRemoteDataSourceImpl(),
    private val userRepository: UserRepository = UserRepositoryImpl()
) : MessageRepository {

    override fun getMessages(
        channelName: String,
        topicName: String,
        lastMessageId: Long,
        numBefore: Int
    ): Single<List<Message>> {
        val narrows = mutableListOf(
            NarrowDto(NarrowDto.OPERATOR_CHANNEL, channelName),
            NarrowDto(NarrowDto.OPERATOR_TOPIC, topicName)
        )

        val anchor = if (lastMessageId <= 0) DEFAULT_ANCHOR else lastMessageId

        val request = Request.Builder()
            .addQuery(ANCHOR_KEY, anchor.toString())
            .addQuery(NUM_BEFORE_KEY, numBefore.toString())
            .addQuery(NUM_AFTER_KEY, DEFAULT_NUM_AFTER)
            .addQuery(NARROW_KEY, Json.encodeToString(narrows))
            .build()

        return messageRemoteDataSource.getMessages(request)
            .flatMap { messagesDto ->
                userRepository.loadMyUser()
                    .map { myUser ->
                        messagesDto.map { messageDto ->
                            messageDto.toMessage(myUser.id)
                        }
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

    companion object {

        private const val DEFAULT_TYPE = "stream"
        private const val DEFAULT_ANCHOR = "newest"
        private const val DEFAULT_NUM_AFTER = "0"
    }
}
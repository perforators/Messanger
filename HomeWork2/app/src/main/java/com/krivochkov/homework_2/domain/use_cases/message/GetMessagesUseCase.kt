package com.krivochkov.homework_2.domain.use_cases.message

import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.domain.repositories.MessageRepository
import io.reactivex.Single

class GetMessagesUseCase(private val messageRepository: MessageRepository) {

    operator fun invoke(
        channelName: String,
        topicName: String,
        lastMessageId: Long = 0,
        numBefore: Int = 1000,
        cached: Boolean = false
    ): Single<List<Message>> {
        return if (cached)
            messageRepository.getCachedMessages(channelName, topicName)
        else
            messageRepository.getMessages(channelName, topicName, lastMessageId, numBefore)
    }
}
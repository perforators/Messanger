package com.krivochkov.homework_2.domain.use_cases.message

import com.krivochkov.homework_2.data.repositories.MessageRepositoryImpl
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.domain.repositories.MessageRepository
import io.reactivex.Single

class GetMessagesUseCase(
    private val messageRepository: MessageRepository = MessageRepositoryImpl()
) {

    operator fun invoke(
        channelName: String,
        topicName: String,
        lastMessageId: Long = 0,
        numBefore: Int = 1000
    ): Single<List<Message>> {
        return messageRepository.getMessages(channelName, topicName, lastMessageId, numBefore)
    }
}
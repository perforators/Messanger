package com.krivochkov.homework_2.domain.use_cases.message

import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.domain.repositories.MessageRepository
import io.reactivex.Single
import javax.inject.Inject

class GetMessagesUseCaseImpl @Inject constructor(
    private val messageRepository: MessageRepository
) : GetMessagesUseCase {

    override operator fun invoke(
        channelName: String,
        topicName: String,
        lastMessageId: Long,
        numBefore: Int,
        cached: Boolean
    ): Single<List<Message>> {
        return if (cached)
            messageRepository.getCachedMessages(channelName, topicName)
        else
            messageRepository.getMessages(channelName, topicName, lastMessageId, numBefore)
    }
}

package com.krivochkov.homework_2.domain.use_cases.message

import com.krivochkov.homework_2.domain.repositories.MessageRepository
import javax.inject.Inject

class GetSingleMessageUseCaseImpl @Inject constructor(
    private val messageRepository: MessageRepository
) : GetSingleMessageUseCase {

    override operator fun invoke(messageId: Long) = messageRepository.getSingleMessage(messageId)
}

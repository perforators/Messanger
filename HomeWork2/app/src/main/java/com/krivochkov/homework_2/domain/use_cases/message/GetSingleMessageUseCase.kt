package com.krivochkov.homework_2.domain.use_cases.message

import com.krivochkov.homework_2.domain.repositories.MessageRepository

class GetSingleMessageUseCase(private val messageRepository: MessageRepository) {

    operator fun invoke(messageId: Long) = messageRepository.getSingleMessage(messageId)
}
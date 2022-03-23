package com.krivochkov.homework_2.domain.use_cases.message

import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.domain.repositories.MessageRepository

class GetAllMessagesUseCase(
    private val repository: MessageRepository
) {

    operator fun invoke(): List<Message> {
        return repository.getAllMessages()
    }
}
package com.krivochkov.homework_2.domain.use_cases.message

import com.krivochkov.homework_2.domain.repositories.MessageRepository

class SendMessageUseCase(
    private val repository: MessageRepository
) {

    operator fun invoke(message: String) {
        repository.sendMessage(message)
    }
}
package com.krivochkov.homework_2.domain.use_cases.message

import com.krivochkov.homework_2.domain.repositories.MessageRepository
import io.reactivex.Completable

class SendMessageUseCase(
    private val repository: MessageRepository
) {

    operator fun invoke(message: String): Completable {
        return repository.sendMessage(message)
    }
}
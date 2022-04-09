package com.krivochkov.homework_2.domain.use_cases.message

import com.krivochkov.homework_2.data.repositories.MessageRepositoryImpl
import com.krivochkov.homework_2.domain.repositories.MessageRepository
import io.reactivex.Completable

class SendMessageUseCase(
    private val repository: MessageRepository = MessageRepositoryImpl()
) {

    operator fun invoke(channelName: String, topicName: String, message: String): Completable {
        return repository.sendMessage(channelName, topicName, message)
    }
}
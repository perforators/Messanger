package com.krivochkov.homework_2.domain.use_cases.reaction

import com.krivochkov.homework_2.data.repositories.MessageRepositoryImpl
import com.krivochkov.homework_2.domain.repositories.MessageRepository
import io.reactivex.Completable

class RemoveReactionUseCase(
    private val repository: MessageRepository = MessageRepositoryImpl()
) {
    operator fun invoke(messageId: Long, emojiName: String): Completable {
        return repository.removeReaction(messageId, emojiName)
    }
}
package com.krivochkov.homework_2.domain.use_cases.reaction

import com.krivochkov.homework_2.domain.repositories.MessageRepository

class UpdateReactionUseCase(
    private val repository: MessageRepository
) {

    operator fun invoke(messageId: Long, emoji: String) {
        repository.updateReaction(messageId, emoji)
    }
}
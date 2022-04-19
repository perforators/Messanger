package com.krivochkov.homework_2.domain.use_cases.reaction

import com.krivochkov.homework_2.domain.repositories.MessageRepository

class AddReactionUseCase(private val repository: MessageRepository) {

    operator fun invoke(messageId: Long, emojiName: String) =
        repository.addReaction(messageId, emojiName)
}
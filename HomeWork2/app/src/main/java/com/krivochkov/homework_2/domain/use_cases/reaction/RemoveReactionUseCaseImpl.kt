package com.krivochkov.homework_2.domain.use_cases.reaction

import com.krivochkov.homework_2.domain.repositories.MessageRepository
import javax.inject.Inject

class RemoveReactionUseCaseImpl @Inject constructor(
    private val repository: MessageRepository
) : RemoveReactionUseCase {

    override operator fun invoke(messageId: Long, emojiName: String) =
        repository.removeReaction(messageId, emojiName)
}

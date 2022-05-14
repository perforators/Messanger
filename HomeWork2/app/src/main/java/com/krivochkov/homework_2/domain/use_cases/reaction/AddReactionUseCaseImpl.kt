package com.krivochkov.homework_2.domain.use_cases.reaction

import com.krivochkov.homework_2.domain.repositories.MessageRepository
import javax.inject.Inject

class AddReactionUseCaseImpl @Inject constructor(
    private val repository: MessageRepository
) : AddReactionUseCase {

    override operator fun invoke(messageId: Long, emojiName: String) =
        repository.addReaction(messageId, emojiName)
}

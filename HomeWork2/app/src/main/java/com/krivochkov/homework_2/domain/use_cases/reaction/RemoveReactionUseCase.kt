package com.krivochkov.homework_2.domain.use_cases.reaction

import io.reactivex.Completable

interface RemoveReactionUseCase {

    operator fun invoke(messageId: Long, emojiName: String): Completable
}
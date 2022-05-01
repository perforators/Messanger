package com.krivochkov.homework_2.domain.use_cases.message

import com.krivochkov.homework_2.domain.models.Message
import io.reactivex.Single

interface GetSingleMessageUseCase {

    operator fun invoke(messageId: Long): Single<Message>
}
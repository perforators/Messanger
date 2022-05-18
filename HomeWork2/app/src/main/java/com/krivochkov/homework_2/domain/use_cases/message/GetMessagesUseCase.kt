package com.krivochkov.homework_2.domain.use_cases.message

import com.krivochkov.homework_2.domain.models.Message
import io.reactivex.Single

interface GetMessagesUseCase {

    operator fun invoke(
        channelName: String,
        topicName: String = "",
        lastMessageId: Long = 0,
        numBefore: Int = 1000,
        cached: Boolean = false
    ): Single<List<Message>>
}
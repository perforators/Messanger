package com.krivochkov.homework_2.domain.use_cases.message

import com.krivochkov.homework_2.domain.models.AttachedFile
import io.reactivex.Completable

interface SendMessageUseCase {

    operator fun invoke(
        channelName: String,
        topicName: String,
        message: String,
        attachedFiles: List<AttachedFile>
    ): Completable
}
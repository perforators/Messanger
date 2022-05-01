package com.krivochkov.homework_2.domain.use_cases.topic

import com.krivochkov.homework_2.domain.models.Topic
import io.reactivex.Single

interface LoadTopicsUseCase {

    operator fun invoke(channelId: Long, cached: Boolean = false): Single<List<Topic>>
}
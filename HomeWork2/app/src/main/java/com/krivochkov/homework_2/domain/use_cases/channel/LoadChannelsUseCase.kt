package com.krivochkov.homework_2.domain.use_cases.channel

import com.krivochkov.homework_2.domain.models.Channel
import io.reactivex.Single

interface LoadChannelsUseCase {

    fun load(cached: Boolean = false): Single<List<Channel>>
}
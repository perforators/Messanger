package com.krivochkov.homework_2.domain.use_cases.channel

import io.reactivex.Completable

interface SubscribeToChannelUseCase {

    operator fun invoke(channelName: String, description: String = ""): Completable
}
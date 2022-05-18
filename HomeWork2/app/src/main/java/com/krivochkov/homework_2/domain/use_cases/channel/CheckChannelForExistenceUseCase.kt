package com.krivochkov.homework_2.domain.use_cases.channel

import io.reactivex.Single

interface CheckChannelForExistenceUseCase {

    operator fun invoke(channelName: String): Single<Boolean>
}
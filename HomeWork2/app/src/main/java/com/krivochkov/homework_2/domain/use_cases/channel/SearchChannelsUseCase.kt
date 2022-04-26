package com.krivochkov.homework_2.domain.use_cases.channel

import com.krivochkov.homework_2.domain.models.Channel
import io.reactivex.Single

class SearchChannelsUseCase(private val loadChannelsUseCase: LoadChannelsUseCase) {

    operator fun invoke(query: String): Single<List<Channel>> =
        loadChannelsUseCase.load()
            .map { channels -> channels.filter { it.name.contains(query) } }
}
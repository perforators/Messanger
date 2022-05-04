package com.krivochkov.homework_2.domain.use_cases.channel

import com.krivochkov.homework_2.domain.models.Channel
import io.reactivex.Single
import javax.inject.Inject

class SearchChannelsUseCaseImpl @Inject constructor(
    private val loadChannelsUseCase: LoadChannelsUseCase
) : SearchChannelsUseCase {

    override operator fun invoke(query: String): Single<List<Channel>> =
        loadChannelsUseCase()
            .map { channels -> channels.filter { it.name.contains(query) } }
}

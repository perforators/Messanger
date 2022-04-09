package com.krivochkov.homework_2.domain.use_cases.channel

import com.krivochkov.homework_2.data.repositories.ChannelRepositoryImpl
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.repositories.ChannelRepository
import com.krivochkov.homework_2.domain.use_cases.SearchableUseCase
import io.reactivex.Single

class LoadAllChannelsUseCase(
    private val repository: ChannelRepository = ChannelRepositoryImpl()
): SearchableUseCase<Channel> {

    override operator fun invoke(filter: ((Channel) -> Boolean)?): Single<List<Channel>> {
        return repository.loadAllChannels()
            .map { messages -> messages.filter { filter?.invoke(it) ?: true } }
    }
}
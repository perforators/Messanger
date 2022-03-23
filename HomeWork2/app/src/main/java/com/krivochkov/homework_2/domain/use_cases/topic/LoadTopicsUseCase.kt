package com.krivochkov.homework_2.domain.use_cases.topic

import com.krivochkov.homework_2.data.repositories.ChannelRepositoryImpl
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.domain.repositories.ChannelRepository

class LoadTopicsUseCase(
    private val repository: ChannelRepository = ChannelRepositoryImpl()
) {

    operator fun invoke(channelId: Long): Pair<Long, List<Topic>> {
        return channelId to repository.loadTopicsInChannel(channelId)
    }
}
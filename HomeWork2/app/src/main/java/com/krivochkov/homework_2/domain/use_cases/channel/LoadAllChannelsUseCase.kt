package com.krivochkov.homework_2.domain.use_cases.channel

import com.krivochkov.homework_2.data.repositories.ChannelRepositoryImpl
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.repositories.ChannelRepository

class LoadAllChannelsUseCase(
    private val repository: ChannelRepository = ChannelRepositoryImpl()
) {

    operator fun invoke(): List<Channel> {
        return repository.loadAllChannels()
    }
}
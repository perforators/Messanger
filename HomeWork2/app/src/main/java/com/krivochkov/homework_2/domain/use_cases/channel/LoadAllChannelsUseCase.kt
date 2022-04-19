package com.krivochkov.homework_2.domain.use_cases.channel

import com.krivochkov.homework_2.domain.repositories.ChannelRepository

class LoadAllChannelsUseCase(private val repository: ChannelRepository) : LoadChannelsUseCase {

    override fun load(cached: Boolean) = repository.getAllChannels(cached)
}
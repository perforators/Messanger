package com.krivochkov.homework_2.domain.use_cases.channel

import com.krivochkov.homework_2.domain.repositories.ChannelRepository

class LoadSubscribedChannelsUseCase(private val repository: ChannelRepository) : LoadChannelsUseCase {

    override fun load(cached: Boolean) = repository.getSubscribedChannels(cached)
}
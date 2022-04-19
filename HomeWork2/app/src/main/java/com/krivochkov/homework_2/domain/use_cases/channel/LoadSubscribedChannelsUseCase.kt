package com.krivochkov.homework_2.domain.use_cases.channel

import com.krivochkov.homework_2.data.repositories.ChannelRepositoryImpl
import com.krivochkov.homework_2.domain.repositories.ChannelRepository

class LoadSubscribedChannelsUseCase(
    private val repository: ChannelRepository = ChannelRepositoryImpl()
) : LoadChannelsUseCase {

    override fun load(cached: Boolean) =
        if (cached) repository.getCachedSubscribedChannels() else repository.getSubscribedChannels()
}
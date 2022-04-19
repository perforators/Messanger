package com.krivochkov.homework_2.domain.use_cases.channel

import com.krivochkov.homework_2.data.repositories.ChannelRepositoryImpl
import com.krivochkov.homework_2.domain.repositories.ChannelRepository

class LoadAllChannelsUseCase(
    private val repository: ChannelRepository = ChannelRepositoryImpl()
) : LoadChannelsUseCase {

    override fun load(cached: Boolean) =
        if (cached) repository.getCachedAllChannels() else repository.getAllChannels()
}
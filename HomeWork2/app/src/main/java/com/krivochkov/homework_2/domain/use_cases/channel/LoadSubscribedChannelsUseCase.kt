package com.krivochkov.homework_2.domain.use_cases.channel

import com.krivochkov.homework_2.domain.repositories.ChannelRepository
import javax.inject.Inject

class LoadSubscribedChannelsUseCase @Inject constructor(
    private val repository: ChannelRepository
) : LoadChannelsUseCase {

    override operator fun invoke(cached: Boolean) = repository.getSubscribedChannels(cached)
}
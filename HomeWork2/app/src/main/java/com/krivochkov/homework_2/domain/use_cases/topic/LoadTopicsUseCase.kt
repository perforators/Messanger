package com.krivochkov.homework_2.domain.use_cases.topic

import com.krivochkov.homework_2.domain.repositories.ChannelRepository

class LoadTopicsUseCase(private val repository: ChannelRepository) {

    operator fun invoke(channelId: Long, cached: Boolean = false) =
        if (cached) repository.getCachedTopics(channelId) else repository.getTopics(channelId)
}
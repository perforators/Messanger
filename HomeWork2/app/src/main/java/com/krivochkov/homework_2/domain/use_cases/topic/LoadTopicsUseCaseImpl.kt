package com.krivochkov.homework_2.domain.use_cases.topic

import com.krivochkov.homework_2.domain.repositories.ChannelRepository
import javax.inject.Inject

class LoadTopicsUseCaseImpl @Inject constructor(
    private val repository: ChannelRepository
) : LoadTopicsUseCase {

    override operator fun invoke(channelId: Long, cached: Boolean) =
        if (cached) repository.getCachedTopics(channelId) else repository.getTopics(channelId)
}

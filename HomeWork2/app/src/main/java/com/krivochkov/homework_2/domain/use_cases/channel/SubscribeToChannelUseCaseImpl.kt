package com.krivochkov.homework_2.domain.use_cases.channel

import com.krivochkov.homework_2.domain.repositories.ChannelRepository
import io.reactivex.Completable
import javax.inject.Inject

class SubscribeToChannelUseCaseImpl @Inject constructor(
    private val repository: ChannelRepository
) : SubscribeToChannelUseCase {

    override fun invoke(channelName: String, description: String): Completable {
        return repository.subscribeToChannel(channelName, description)
    }
}
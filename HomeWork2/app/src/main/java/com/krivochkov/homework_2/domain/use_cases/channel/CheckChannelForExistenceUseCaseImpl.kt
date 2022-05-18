package com.krivochkov.homework_2.domain.use_cases.channel

import com.krivochkov.homework_2.domain.repositories.ChannelRepository
import io.reactivex.Single
import javax.inject.Inject

class CheckChannelForExistenceUseCaseImpl @Inject constructor(
    private val repository: ChannelRepository
) : CheckChannelForExistenceUseCase {

    override fun invoke(channelName: String): Single<Boolean> {
        return repository.getChannelId(channelName)
            .map { id -> id != -1L }
    }
}
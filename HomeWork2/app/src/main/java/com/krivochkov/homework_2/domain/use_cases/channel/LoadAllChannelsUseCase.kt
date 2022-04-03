package com.krivochkov.homework_2.domain.use_cases.channel

import com.krivochkov.homework_2.data.repositories.ChannelRepositoryImpl
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.repositories.ChannelRepository
import io.reactivex.Observable

class LoadAllChannelsUseCase(
    private val repository: ChannelRepository = ChannelRepositoryImpl()
) {

    operator fun invoke(filter: ((Channel) -> Boolean)? = null): Observable<Channel> {
        return repository.loadAllChannels()
            .flatMapObservable { Observable.fromIterable(it) }
            .filter { filter?.invoke(it) ?: true }
    }
}
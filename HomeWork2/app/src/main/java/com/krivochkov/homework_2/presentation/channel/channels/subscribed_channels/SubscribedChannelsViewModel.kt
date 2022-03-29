package com.krivochkov.homework_2.presentation.channel.channels.subscribed_channels

import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.use_cases.channel.LoadSubscribedChannelsUseCase
import com.krivochkov.homework_2.presentation.channel.channels.BaseChannelsViewModel
import io.reactivex.Observable

class SubscribedChannelsViewModel(
    private val loadSubscribedChannelsUseCase: LoadSubscribedChannelsUseCase = LoadSubscribedChannelsUseCase()
) : BaseChannelsViewModel() {

    init {
        loadChannels()
    }

    override fun searchChannels(query: String): Observable<Channel> {
        return loadSubscribedChannelsUseCase { it.name.contains(query) }
    }
}
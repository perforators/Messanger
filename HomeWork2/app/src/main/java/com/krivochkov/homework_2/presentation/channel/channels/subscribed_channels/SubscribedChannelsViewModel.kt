package com.krivochkov.homework_2.presentation.channel.channels.subscribed_channels

import com.krivochkov.homework_2.domain.use_cases.channel.LoadSubscribedChannelsUseCase
import com.krivochkov.homework_2.presentation.channel.channels.BaseChannelsViewModel

class SubscribedChannelsViewModel(
    private val loadSubscribedChannelsUseCase: LoadSubscribedChannelsUseCase = LoadSubscribedChannelsUseCase()
) : BaseChannelsViewModel() {

    init {
        loadChannels()
    }

    override fun loadChannels() {
        _channels.value = loadSubscribedChannelsUseCase()
    }
}
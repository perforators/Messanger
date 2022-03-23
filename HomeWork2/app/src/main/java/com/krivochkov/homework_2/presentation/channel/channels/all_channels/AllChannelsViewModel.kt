package com.krivochkov.homework_2.presentation.channel.channels.all_channels

import com.krivochkov.homework_2.domain.use_cases.channel.LoadAllChannelsUseCase
import com.krivochkov.homework_2.presentation.channel.channels.BaseChannelsViewModel

class AllChannelsViewModel(
    private val loadAllChannelsUseCase: LoadAllChannelsUseCase = LoadAllChannelsUseCase()
) : BaseChannelsViewModel() {

    init {
        loadChannels()
    }

    override fun loadChannels() {
        _channels.value = loadAllChannelsUseCase()
    }
}
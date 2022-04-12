package com.krivochkov.homework_2.presentation.channel.channels.subscribed_channels

import com.krivochkov.homework_2.domain.use_cases.channel.LoadSubscribedChannelsUseCase
import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCase
import com.krivochkov.homework_2.presentation.channel.channels.BaseChannelsViewModel

class SubscribedChannelsViewModel(
    loadSubscribedChannelsUseCase: LoadSubscribedChannelsUseCase = LoadSubscribedChannelsUseCase(),
    loadTopicsUseCase: LoadTopicsUseCase = LoadTopicsUseCase()
) : BaseChannelsViewModel(loadTopicsUseCase, loadSubscribedChannelsUseCase)
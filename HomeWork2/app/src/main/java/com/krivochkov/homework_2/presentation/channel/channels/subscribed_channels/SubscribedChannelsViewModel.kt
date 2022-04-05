package com.krivochkov.homework_2.presentation.channel.channels.subscribed_channels

import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.use_cases.SearchableUseCase
import com.krivochkov.homework_2.domain.use_cases.channel.LoadSubscribedChannelsUseCase
import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCase
import com.krivochkov.homework_2.presentation.channel.channels.BaseChannelsViewModel

class SubscribedChannelsViewModel(
    searchableUseCase: SearchableUseCase<Channel> = LoadSubscribedChannelsUseCase(),
    loadTopicsUseCase: LoadTopicsUseCase = LoadTopicsUseCase()
) : BaseChannelsViewModel(searchableUseCase, loadTopicsUseCase)
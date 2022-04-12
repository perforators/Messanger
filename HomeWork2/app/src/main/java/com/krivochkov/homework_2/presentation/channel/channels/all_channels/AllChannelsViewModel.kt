package com.krivochkov.homework_2.presentation.channel.channels.all_channels

import com.krivochkov.homework_2.domain.use_cases.channel.LoadAllChannelsUseCase
import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCase
import com.krivochkov.homework_2.presentation.channel.channels.BaseChannelsViewModel

class AllChannelsViewModel(
    loadAllChannelsUseCase: LoadAllChannelsUseCase = LoadAllChannelsUseCase(),
    loadTopicsUseCase: LoadTopicsUseCase = LoadTopicsUseCase()
) : BaseChannelsViewModel(loadTopicsUseCase, loadAllChannelsUseCase)
package com.krivochkov.homework_2.presentation.channel.channels.all_channels

import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.use_cases.channel.LoadAllChannelsUseCase
import com.krivochkov.homework_2.presentation.channel.channels.BaseChannelsViewModel
import io.reactivex.Observable

class AllChannelsViewModel(
    private val loadAllChannelsUseCase: LoadAllChannelsUseCase = LoadAllChannelsUseCase()
) : BaseChannelsViewModel() {

    init {
        loadChannels()
    }

    override fun searchChannels(query: String): Observable<Channel> {
        return loadAllChannelsUseCase { it.name.contains(query) }
    }
}
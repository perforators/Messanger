package com.krivochkov.homework_2.presentation.channel.channels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.krivochkov.homework_2.presentation.SearchQueryFilter
import com.krivochkov.homework_2.presentation.channel.channels.elm.ChannelStoreFactory

class ChannelsViewModelFactory(
    private val channelsStoreFactory: ChannelStoreFactory,
    private val searchQueryFilter: SearchQueryFilter
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChannelsViewModel(channelsStoreFactory, searchQueryFilter) as T
    }
}
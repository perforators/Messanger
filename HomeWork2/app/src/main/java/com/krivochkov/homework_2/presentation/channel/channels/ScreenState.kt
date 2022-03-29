package com.krivochkov.homework_2.presentation.channel.channels

import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.ChannelItem

sealed class ScreenState {
    data class ChannelsLoaded(val channels: List<ChannelItem>) : ScreenState()
    object Loading : ScreenState()
    object Error : ScreenState()
}
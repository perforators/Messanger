package com.krivochkov.homework_2.presentation.channel.channels

import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.TopicItem

sealed class UIEvent {
    data class SubmitTopicsInChannel(val channelId: Long, val topics: List<TopicItem>) : UIEvent()
    object FailedLoadTopics : UIEvent()
}
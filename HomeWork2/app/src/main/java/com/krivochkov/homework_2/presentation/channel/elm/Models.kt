package com.krivochkov.homework_2.presentation.channel.elm

import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.ChannelItem
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.TopicItem

data class ChannelState(
    val channels: List<ChannelItem> = emptyList(),
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val isInitialized: Boolean = false,
    val lastQuery: String = ""
)

sealed class ChannelEvent {

    sealed class Ui : ChannelEvent() {
        object Init : Ui()
        data class TopicClick(val channel: Channel, val topic: Topic) : Ui()
        data class SearchChannels(val query: String) : Ui()
        object SearchChannelsByLastQuery : Ui()
        data class ExpandChannel(val channelId: Long) : Ui()
        data class CollapseChannel(val channelId: Long) : Ui()
    }

    sealed class Internal : ChannelEvent() {
        data class CachedChannelsLoaded(val channels: List<ChannelItem>) : Internal()
        data class ChannelsFound(val channels: List<ChannelItem>) : Internal()
        data class TopicsLoaded(val channelId: Long, val topics: List<TopicItem>) : Internal()
        data class ErrorLoadingCachedChannels(val error: Throwable) : Internal()
        data class ErrorSearchChannels(val error: Throwable) : Internal()
        data class ErrorLoadingTopics(val channelId: Long, val error: Throwable) : Internal()
    }
}

sealed class ChannelEffect {
    object ShowErrorLoadingTopics : ChannelEffect()
    object ShowErrorLoadingCachedChannels : ChannelEffect()
    data class ShowTopicContent(val channel: Channel, val topic: Topic) : ChannelEffect()
}

sealed class ChannelCommand {
    object LoadCachedChannels : ChannelCommand()
    data class SearchChannels(val query: String = "") : ChannelCommand()
    data class LoadTopics(val channelId: Long, val cached: Boolean) : ChannelCommand()
}

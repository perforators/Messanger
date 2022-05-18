package com.krivochkov.homework_2.presentation.channel.channels.elm

import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.ChannelItem
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.TopicItem

data class ChannelState(
    val items: List<Item> = emptyList(),
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val isInitialized: Boolean = false,
    val lastQuery: String = ""
)

sealed class ChannelEvent {

    sealed class Ui : ChannelEvent() {
        object Init : Ui()
        data class TopicClick(val channel: Channel, val topic: Topic) : Ui()
        data class ChannelClick(val channel: Channel) : Ui()
        data class SearchChannels(val query: String) : Ui()
        object CreatingChannelItemClick : Ui()
        object SearchChannelsByLastQuery : Ui()
        data class ExpandChannel(val channelId: Long) : Ui()
        data class CollapseChannel(val channelId: Long) : Ui()
    }

    sealed class Internal : ChannelEvent() {
        data class CachedChannelsLoaded(val channels: List<ChannelItem>) : Internal()
        data class ChannelsFound(val channels: List<ChannelItem>) : Internal()
        data class CachedTopicsLoaded(val channelId: Long, val topics: List<TopicItem>) : Internal()
        data class ActualTopicsLoaded(val channelId: Long, val topics: List<TopicItem>) : Internal()
        data class ErrorLoadingCachedChannels(val error: Throwable) : Internal()
        data class ErrorSearchChannels(val error: Throwable) : Internal()
        data class ErrorLoadingCachedTopics(val channelId: Long, val error: Throwable) : Internal()
        data class ErrorLoadingActualTopics(val channelId: Long, val error: Throwable) : Internal()
    }
}

sealed class ChannelEffect {
    object ShowErrorLoadingCachedTopics : ChannelEffect()
    object ShowErrorLoadingActualTopics : ChannelEffect()
    object ShowCreateChannelScreen : ChannelEffect()
    object ShowErrorLoadingCachedChannels : ChannelEffect()
    object ShowErrorSearchingActualChannels : ChannelEffect()
    data class ShowTopicContent(val channel: Channel, val topic: Topic) : ChannelEffect()
    data class ShowChannelContent(val channel: Channel) : ChannelEffect()
}

sealed class ChannelCommand {
    object LoadCachedChannels : ChannelCommand()
    data class SearchChannels(val query: String = "") : ChannelCommand()
    data class LoadCachedTopics(val channelId: Long) : ChannelCommand()
    data class LoadActualTopics(val channelId: Long) : ChannelCommand()
}

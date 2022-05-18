package com.krivochkov.homework_2.presentation.channel.channels.elm

import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.ChannelItem
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.CreateChannelItem
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.LoadingItem
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.TopicItem
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

class ChannelReducer : ScreenDslReducer<ChannelEvent, ChannelEvent.Ui, ChannelEvent.Internal, ChannelState, ChannelEffect, ChannelCommand>(
    ChannelEvent.Ui::class,
    ChannelEvent.Internal::class
) {

    override fun Result.internal(event: ChannelEvent.Internal): Any {
        return when (event) {
            is ChannelEvent.Internal.ErrorLoadingCachedChannels -> {
                effects { +ChannelEffect.ShowErrorLoadingCachedChannels }
                commands { +ChannelCommand.SearchChannels() }
            }
            is ChannelEvent.Internal.ErrorSearchChannels -> {
                if (state.items.isEmpty()) {
                    state { copy(isLoading = false, error = event.error) }
                } else {
                    state { copy(isLoading = false) }
                    effects { +ChannelEffect.ShowErrorSearchingActualChannels }
                }
            }
            is ChannelEvent.Internal.ErrorLoadingActualTopics -> {
                val items = state.items.toMutableList()
                items.replaceChannelItem(event.channelId) { channelItem ->
                    if (channelItem.childItems.isEmpty() ||
                        channelItem.childItems.contains(LoadingItem)) {
                        channelItem.copy(isExpanded = false, childItems = emptyList())
                    } else {
                        channelItem
                    }
                }
                state { copy(items = items) }
                effects { +ChannelEffect.ShowErrorLoadingActualTopics }
            }
            is ChannelEvent.Internal.ErrorLoadingCachedTopics -> {
                effects { +ChannelEffect.ShowErrorLoadingCachedTopics }
                commands { ChannelCommand.LoadActualTopics(event.channelId) }
            }
            is ChannelEvent.Internal.CachedChannelsLoaded -> {
                state {
                    copy(
                        isLoading = event.channels.isEmpty(),
                        error = null,
                        items = event.channels
                    )
                }
                commands { +ChannelCommand.SearchChannels() }
            }
            is ChannelEvent.Internal.ChannelsFound -> {
                state {
                    copy(
                        isLoading = false,
                        error = null,
                        items = listOf(CreateChannelItem) + event.channels
                    )
                }
            }
            is ChannelEvent.Internal.ActualTopicsLoaded -> {
                addTopicsToChannel(event.channelId, event.topics)
            }
            is ChannelEvent.Internal.CachedTopicsLoaded -> {
                addTopicsToChannel(event.channelId, event.topics)
                commands { +ChannelCommand.LoadActualTopics(event.channelId) }
            }
        }
    }

    override fun Result.ui(event: ChannelEvent.Ui): Any {
        return when (event) {
            is ChannelEvent.Ui.Init -> {
                if (state.isInitialized.not()) {
                    state { copy(isLoading = true, isInitialized = true, error = null) }
                    commands { +ChannelCommand.LoadCachedChannels }
                } else {
                    Any()
                }
            }
            is ChannelEvent.Ui.CreatingChannelItemClick -> {
                effects { +ChannelEffect.ShowCreateChannelScreen }
            }
            is ChannelEvent.Ui.TopicClick -> {
                effects { +ChannelEffect.ShowTopicContent(event.channel, event.topic) }
            }
            is ChannelEvent.Ui.ChannelClick -> {
                effects { +ChannelEffect.ShowChannelContent(event.channel) }
            }
            is ChannelEvent.Ui.SearchChannels -> {
                state { copy(isLoading = true, error = null, lastQuery = event.query) }
                commands { +ChannelCommand.SearchChannels(event.query) }
            }
            is ChannelEvent.Ui.SearchChannelsByLastQuery -> {
                state { copy(isLoading = true, error = null) }
                commands { +ChannelCommand.SearchChannels(state.lastQuery) }
            }
            is ChannelEvent.Ui.ExpandChannel -> {
                val items = state.items.toMutableList()
                items.replaceChannelItem(event.channelId) { channelItem ->
                    channelItem.copy(isExpanded = true, childItems = listOf(LoadingItem))
                }
                state { copy(items = items) }
                commands {
                    +ChannelCommand.LoadCachedTopics(event.channelId)
                }
            }
            is ChannelEvent.Ui.CollapseChannel -> {
                val items = state.items.toMutableList()
                items.replaceChannelItem(event.channelId) { channelItem ->
                    channelItem.copy(isExpanded = false, childItems = emptyList())
                }
                state { copy(items = items) }
            }
        }
    }

    private fun Result.addTopicsToChannel(channelId: Long, topics: List<TopicItem>) {
        val channelItem = state.items.find {
            it is ChannelItem && it.channel.id == channelId
        } as? ChannelItem

        if (channelItem != null && channelItem.isExpanded && topics.isNotEmpty()) {
            val items = state.items.toMutableList()
            items.replaceChannelItem(channelId) {
                it.copy(childItems = topics)
            }
            state { copy(items = items) }
        }
    }

    private fun MutableList<Item>.replaceChannelItem(
        channelId: Long,
        replacer: (ChannelItem) -> ChannelItem
    ) {
        replaceAll { item ->
            if (item is ChannelItem && item.channel.id == channelId) {
                replacer(item)
            } else {
                item
            }
        }
    }
}

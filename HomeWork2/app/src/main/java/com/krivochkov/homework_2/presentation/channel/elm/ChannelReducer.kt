package com.krivochkov.homework_2.presentation.channel.elm

import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.ChannelItem
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.LoadingItem
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

class ChannelReducer : ScreenDslReducer<ChannelEvent, ChannelEvent.Ui, ChannelEvent.Internal, ChannelState, ChannelEffect, ChannelCommand>(
    ChannelEvent.Ui::class,
    ChannelEvent.Internal::class
) {

    override fun Result.internal(event: ChannelEvent.Internal): Any {
        return when (event) {
            is ChannelEvent.Internal.ErrorLoadingCachedChannels -> {
                effects { +ChannelEffect.ShowErrorLoadingCachedChannels }
            }
            is ChannelEvent.Internal.ErrorSearchChannels -> {
                state { copy(isLoading = false, error = event.error) }
            }
            is ChannelEvent.Internal.ErrorLoadingTopics -> {
                val channels = state.channels.toMutableList()
                channels.replaceChannelItem(event.channelId) { channelItem ->
                    channelItem.copy(isExpanded = false, childItems = emptyList())
                }
                state { copy(channels = channels) }
                effects { +ChannelEffect.ShowErrorLoadingTopics }
            }
            is ChannelEvent.Internal.CachedChannelsLoaded -> {
                state {
                    copy(
                        isLoading = event.channels.isEmpty(),
                        error = null,
                        channels = event.channels
                    )
                }
            }
            is ChannelEvent.Internal.ChannelsFound -> {
                state { copy(isLoading = false, error = null, channels = event.channels) }
            }
            is ChannelEvent.Internal.TopicsLoaded -> {
                val channelItem = state.channels.find { it.channel.id == event.channelId }
                if (channelItem != null && channelItem.isExpanded && event.topics.isNotEmpty()) {
                    val channels = state.channels.toMutableList()
                    channels.replaceChannelItem(event.channelId) {
                        it.copy(childItems = event.topics)
                    }
                    state { copy(channels = channels) }
                } else {
                    Any()
                }
            }
        }
    }

    override fun Result.ui(event: ChannelEvent.Ui): Any {
        return when (event) {
            is ChannelEvent.Ui.Init -> {
                // чтобы при изменении конфигурации не скачивать снова каналы
                if (state.isInitialized.not()) {
                    state { ChannelState(isLoading = true, isInitialized = true) }
                    commands {
                        +ChannelCommand.LoadCachedChannels
                        +ChannelCommand.SearchChannels()
                    }
                } else {
                    Any()
                }
            }
            is ChannelEvent.Ui.TopicClick -> {
                effects { +ChannelEffect.ShowTopicContent(event.channel, event.topic) }
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
                val channels = state.channels.toMutableList()
                channels.replaceChannelItem(event.channelId) { channelItem ->
                    channelItem.copy(isExpanded = true, childItems = listOf(LoadingItem))
                }
                state { copy(channels = channels) }
                commands {
                    +ChannelCommand.LoadTopics(event.channelId, cached = true)
                    +ChannelCommand.LoadTopics(event.channelId, cached = false)
                }
            }
            is ChannelEvent.Ui.CollapseChannel -> {
                val channels = state.channels.toMutableList()
                channels.replaceChannelItem(event.channelId) { channelItem ->
                    channelItem.copy(isExpanded = false, childItems = emptyList())
                }
                state { copy(channels = channels) }
            }
        }
    }

    private fun MutableList<ChannelItem>.replaceChannelItem(
        channelId: Long,
        replacer: (ChannelItem) -> ChannelItem
    ) {
        replaceAll { channelItem ->
            if (channelItem.channel.id == channelId)
                replacer(channelItem)
            else
                channelItem
        }
    }
}

package com.krivochkov.homework_2.presentation.channel.channels.elm

import com.krivochkov.homework_2.domain.use_cases.channel.LoadChannelsUseCase
import com.krivochkov.homework_2.domain.use_cases.channel.SearchChannelsUseCase
import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCase
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.ChannelItem
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.TopicItem
import com.krivochkov.homework_2.presentation.elm_core.Switcher
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

class ChannelActor(
    private val loadChannelsUseCase: LoadChannelsUseCase,
    private val searchChannelsUseCase: SearchChannelsUseCase,
    private val loadTopicsUseCase: LoadTopicsUseCase
) : ActorCompat<ChannelCommand, ChannelEvent> {

    private val switcher = Switcher()

    override fun execute(command: ChannelCommand): Observable<ChannelEvent> = when (command) {
        is ChannelCommand.LoadCachedChannels -> loadChannelsUseCase(cached = true)
            .map { channels -> channels.map { channel -> ChannelItem(channel) } }
            .mapEvents(
                { list -> ChannelEvent.Internal.CachedChannelsLoaded(list) },
                { error -> ChannelEvent.Internal.ErrorLoadingCachedChannels(error) }
            )
        is ChannelCommand.SearchChannels -> switcher.observable {
            searchChannelsUseCase(command.query)
                .map { channels -> channels.map { channel -> ChannelItem(channel) } }
                .mapEvents(
                    { list -> ChannelEvent.Internal.ChannelsFound(list) },
                    { error -> ChannelEvent.Internal.ErrorSearchChannels(error) }
                )
        }
        is ChannelCommand.LoadCachedTopics -> loadTopicsUseCase(command.channelId, cached = true)
            .map { topics -> topics.map { topic -> TopicItem(topic, command.channelId) } }
            .mapEvents(
                { list -> ChannelEvent.Internal.CachedTopicsLoaded(command.channelId, list) },
                { error -> ChannelEvent.Internal.ErrorLoadingCachedTopics(command.channelId, error) }
            )
        is ChannelCommand.LoadActualTopics -> loadTopicsUseCase(command.channelId, cached = false)
            .map { topics -> topics.map { topic -> TopicItem(topic, command.channelId) } }
            .mapEvents(
                { list -> ChannelEvent.Internal.ActualTopicsLoaded(command.channelId, list) },
                { error -> ChannelEvent.Internal.ErrorLoadingActualTopics(command.channelId, error) }
            )
    }
}

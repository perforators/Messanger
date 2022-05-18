package com.krivochkov.homework_2.presentation.chat.topic_pick.elm

import com.krivochkov.homework_2.di.topic_pick.annotations.TopicPickScreenScope
import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCase
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.TopicItem
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

@TopicPickScreenScope
class TopicPickActor(
    private val loadTopicsUseCase: LoadTopicsUseCase
) : ActorCompat<TopicPickCommand, TopicPickEvent> {

    override fun execute(command: TopicPickCommand): Observable<TopicPickEvent> = when (command) {
        is TopicPickCommand.LoadTopics -> loadTopicsUseCase(command.channelId)
            .map { topics -> topics.map { topic -> TopicItem(topic, command.channelId) } }
            .mapEvents(
                { list -> TopicPickEvent.Internal.TopicsLoaded(list) },
                { error -> TopicPickEvent.Internal.ErrorLoadingTopics(error) }
            )
    }
}
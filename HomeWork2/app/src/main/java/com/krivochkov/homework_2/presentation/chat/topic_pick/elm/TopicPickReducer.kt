package com.krivochkov.homework_2.presentation.chat.topic_pick.elm

import com.krivochkov.homework_2.presentation.chat.topic_pick.adapter.items.CreateTopicItem
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

class TopicPickReducer :
    ScreenDslReducer<TopicPickEvent, TopicPickEvent.Ui, TopicPickEvent.Internal, TopicPickState, TopicPickEffect, TopicPickCommand>(
        TopicPickEvent.Ui::class,
        TopicPickEvent.Internal::class
    ) {

    override fun Result.internal(event: TopicPickEvent.Internal): Any {
        return when (event) {
            is TopicPickEvent.Internal.TopicsLoaded -> {
                state { copy(isLoading = false, topics = listOf(CreateTopicItem) + event.topics) }
            }
            is TopicPickEvent.Internal.ErrorLoadingTopics -> {
                state { copy(isLoading = false) }
                effects {
                    +TopicPickEffect.ShowErrorLoadingTopics
                    +TopicPickEffect.NavigateUp
                }
            }
        }
    }

    override fun Result.ui(event: TopicPickEvent.Ui): Any {
        return when (event) {
            is TopicPickEvent.Ui.Init -> {
                if (state.isInitialized.not()) {
                    state { copy(isLoading = true, isInitialized = true) }
                    commands { +TopicPickCommand.LoadTopics(event.channelId) }
                } else {
                    Any()
                }
            }
            is TopicPickEvent.Ui.NavigateUp -> {
                effects { +TopicPickEffect.NavigateUp }
            }
            is TopicPickEvent.Ui.TopicClick -> {
                effects {
                    if (event.topic.name.trim().isEmpty()) {
                        +TopicPickEffect.ShowErrorValidationTopic
                    } else {
                        +TopicPickEffect.SendTopicToChat(event.topic)
                        +TopicPickEffect.NavigateUp
                    }
                }
            }
        }
    }
}
package com.krivochkov.homework_2.presentation.chat.topic_pick.elm

import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.TopicItem

data class TopicPickState(
    val topics: List<Item> = emptyList(),
    val isLoading: Boolean = false,
    val isInitialized: Boolean = false
)

sealed class TopicPickEvent {

    sealed class Ui : TopicPickEvent() {
        data class Init(val channelId: Long) : Ui()
        data class TopicClick(val topic: Topic) : Ui()
        object NavigateUp : Ui()
    }

    sealed class Internal : TopicPickEvent() {
        data class TopicsLoaded(val topics: List<TopicItem>) : Internal()
        data class ErrorLoadingTopics(val error: Throwable) : Internal()
    }
}

sealed class TopicPickEffect {
    data class SendTopicToChat(val topic: Topic) : TopicPickEffect()
    object ShowErrorLoadingTopics : TopicPickEffect()
    object ShowErrorValidationTopic : TopicPickEffect()
    object NavigateUp : TopicPickEffect()
}

sealed class TopicPickCommand {
    data class LoadTopics(val channelId: Long) : TopicPickCommand()
}
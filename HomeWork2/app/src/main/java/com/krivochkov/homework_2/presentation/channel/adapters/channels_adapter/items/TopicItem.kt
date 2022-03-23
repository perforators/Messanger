package com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items

import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.view_holders.TopicViewHolder.Companion.TYPE_TOPIC

data class TopicItem(val topic: Topic, val associatedChannelId: Long) : Item {

    override fun areItemsTheSame(otherItem: Item) =
        otherItem is TopicItem && topic.id == otherItem.topic.id

    override fun areContentsTheSame(otherItem: Item) =
        otherItem is TopicItem && this == otherItem

    override fun getType() = TYPE_TOPIC
}
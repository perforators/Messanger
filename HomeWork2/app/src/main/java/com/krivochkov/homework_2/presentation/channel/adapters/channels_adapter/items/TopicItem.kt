package com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items

import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.domain.models.Topic

data class TopicItem(val topic: Topic, val associatedChannelId: Long) : Item {

    override fun areItemsTheSame(otherItem: Item) = areContentsTheSame(otherItem)

    override fun areContentsTheSame(otherItem: Item) =
        otherItem is TopicItem && this == otherItem

    override fun getType() = TYPE

    companion object {
        const val TYPE = 1
    }
}
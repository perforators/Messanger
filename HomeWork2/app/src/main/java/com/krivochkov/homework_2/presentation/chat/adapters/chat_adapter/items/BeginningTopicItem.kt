package com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.items

import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.Item

class BeginningTopicItem(val topic: Topic) : Item {

    override fun areItemsTheSame(otherItem: Item) = areContentsTheSame(otherItem)

    override fun areContentsTheSame(otherItem: Item) =
        otherItem is BeginningTopicItem && topic == otherItem.topic

    override fun getType() = TYPE

    companion object {
        const val TYPE = 8
    }
}
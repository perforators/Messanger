package com.krivochkov.homework_2.presentation.chat.topic_pick.adapter.items

import com.krivochkov.homework_2.presentation.Item

object CreateTopicItem : Item {

    override fun areItemsTheSame(otherItem: Item) = otherItem is CreateTopicItem

    override fun areContentsTheSame(otherItem: Item) = otherItem is CreateTopicItem

    override fun getType() = TYPE

    const val TYPE = 10
}
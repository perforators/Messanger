package com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items

import com.krivochkov.homework_2.presentation.Item

object CreateChannelItem : Item {

    override fun areItemsTheSame(otherItem: Item) = otherItem is CreateChannelItem

    override fun areContentsTheSame(otherItem: Item) = otherItem is CreateChannelItem

    override fun getType() = TYPE

    const val TYPE = 7
}
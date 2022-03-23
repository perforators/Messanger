package com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items

import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.view_holders.ChannelViewHolder.Companion.TYPE_CHANNEL

data class ChannelItem(
    val channel: Channel,
    var isExpanded: Boolean = false,
    var childItems: List<Item> = emptyList()
) : Item {

    override fun areItemsTheSame(otherItem: Item) =
        otherItem is ChannelItem && channel.id == otherItem.channel.id

    override fun areContentsTheSame(otherItem: Item) =
        otherItem is ChannelItem && this == otherItem

    override fun getType() = TYPE_CHANNEL
}
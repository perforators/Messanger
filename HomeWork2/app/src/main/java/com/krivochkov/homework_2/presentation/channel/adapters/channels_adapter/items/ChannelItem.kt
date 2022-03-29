package com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items

import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.presentation.Item

data class ChannelItem(
    val channel: Channel,
    val isExpanded: Boolean = false,
    val childItems: List<Item> = emptyList()
) : Item {

    override fun areItemsTheSame(otherItem: Item) =
        otherItem is ChannelItem && channel.id == otherItem.channel.id

    override fun areContentsTheSame(otherItem: Item) =
        otherItem is ChannelItem && this == otherItem

    override fun getType() = TYPE

    companion object {
        const val TYPE = 0
    }
}
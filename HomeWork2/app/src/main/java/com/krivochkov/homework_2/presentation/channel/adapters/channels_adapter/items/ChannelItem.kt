package com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items

import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.ChannelsAdapter.Companion.TYPE_CHANNEL
import com.krivochkov.homework_2.domain.models.Channel

class ChannelItem(val channel: Channel, ) : Item {

    var isExpanded: Boolean = false
    var topicItems: List<TopicItem> = emptyList()

    override fun areItemsTheSame(otherItem: Item) =
        otherItem is ChannelItem && channel.id == otherItem.channel.id

    override fun areContentsTheSame(otherItem: Item) =
        otherItem is ChannelItem && channel == otherItem.channel

    override fun getType() = TYPE_CHANNEL
}
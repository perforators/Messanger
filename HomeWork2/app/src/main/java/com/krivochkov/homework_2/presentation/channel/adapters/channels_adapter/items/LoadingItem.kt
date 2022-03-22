package com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items

import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.ChannelsAdapter.Companion.TYPE_LOADING

class LoadingItem : ChildItem() {

    override fun areItemsTheSame(otherItem: Item) = otherItem is LoadingItem

    override fun areContentsTheSame(otherItem: Item) = otherItem is LoadingItem

    override fun getType() = TYPE_LOADING
}
package com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items

import com.krivochkov.homework_2.presentation.Item

object LoadingItem : Item {

    override fun areItemsTheSame(otherItem: Item) = otherItem is LoadingItem

    override fun areContentsTheSame(otherItem: Item) = otherItem is LoadingItem

    override fun getType() = TYPE

    const val TYPE = 2
}
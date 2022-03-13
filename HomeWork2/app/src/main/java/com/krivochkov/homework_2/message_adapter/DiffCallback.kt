package com.krivochkov.homework_2.message_adapter

import androidx.recyclerview.widget.DiffUtil
import com.krivochkov.homework_2.message_adapter.items.Item

class DiffCallback : DiffUtil.ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item) =
        oldItem::class == newItem::class && oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Item, newItem: Item) =
        oldItem::class == newItem::class && oldItem.compareTo(newItem)
}
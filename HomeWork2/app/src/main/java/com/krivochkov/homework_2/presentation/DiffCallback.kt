package com.krivochkov.homework_2.presentation

import androidx.recyclerview.widget.DiffUtil

class DiffCallback : DiffUtil.ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item) =
        oldItem.areItemsTheSame(newItem)

    override fun areContentsTheSame(oldItem: Item, newItem: Item) =
        oldItem.areContentsTheSame(newItem)
}
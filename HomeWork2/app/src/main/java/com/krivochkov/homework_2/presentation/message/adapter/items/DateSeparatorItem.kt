package com.krivochkov.homework_2.presentation.message.adapter.items

import com.krivochkov.homework_2.presentation.Item

class DateSeparatorItem(val date: String) : Item {

    override fun areItemsTheSame(otherItem: Item) = areContentsTheSame(otherItem)

    override fun areContentsTheSame(otherItem: Item) =
        otherItem is DateSeparatorItem && date == otherItem.date

    override fun getType() = TYPE

    companion object {
        const val TYPE = 4
    }
}
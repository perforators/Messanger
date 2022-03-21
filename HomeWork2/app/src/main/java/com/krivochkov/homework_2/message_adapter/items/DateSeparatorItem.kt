package com.krivochkov.homework_2.message_adapter.items

import com.krivochkov.homework_2.message_adapter.MessageAdapter.Companion.TYPE_DATE_SEPARATOR

class DateSeparatorItem(val date: String) : Item {

    override fun areItemsTheSame(otherItem: Item) =
        areContentsTheSame(otherItem)

    override fun areContentsTheSame(otherItem: Item) =
        otherItem is DateSeparatorItem && date == otherItem.date

    override fun getType() = TYPE_DATE_SEPARATOR

}
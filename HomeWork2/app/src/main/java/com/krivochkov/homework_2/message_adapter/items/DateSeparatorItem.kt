package com.krivochkov.homework_2.message_adapter.items

class DateSeparatorItem(override val id: Long, val date: String) : Item() {

    override fun getType() = TYPE_DATE_SEPARATOR

    override fun compareTo(otherItem: Item) = if (otherItem is DateSeparatorItem) {
        date == otherItem.date
    } else {
        false
    }
}
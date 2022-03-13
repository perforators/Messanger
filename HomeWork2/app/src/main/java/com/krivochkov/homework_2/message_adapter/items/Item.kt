package com.krivochkov.homework_2.message_adapter.items

abstract class Item {

    abstract val id: Long

    abstract fun getType(): Int

    abstract fun compareTo(otherItem: Item): Boolean

    companion object {
        const val TYPE_MESSAGE = 0
        const val TYPE_DATE_SEPARATOR = 1
    }
}
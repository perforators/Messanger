package com.krivochkov.homework_2.message_adapter.items

import com.krivochkov.homework_2.models.Message

class MessageItem(val message: Message) : Item() {

    override val id: Long
        get() = message.id

    override fun getType() = TYPE_MESSAGE

    override fun compareTo(otherItem: Item) = if (otherItem is MessageItem) {
        message == otherItem.message
    } else {
        false
    }
}
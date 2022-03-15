package com.krivochkov.homework_2.message_adapter.items

import com.krivochkov.homework_2.message_adapter.MessageAdapter.Companion.TYPE_DATE_SEPARATOR
import com.krivochkov.homework_2.models.Message

class MessageItem(val message: Message) : Item {

    override fun areItemsTheSame(otherItem: Item) =
        otherItem is MessageItem && message.id == otherItem.message.id

    override fun areContentsTheSame(otherItem: Item) =
        otherItem is MessageItem && message == otherItem.message

    override fun getType() = TYPE_DATE_SEPARATOR
}
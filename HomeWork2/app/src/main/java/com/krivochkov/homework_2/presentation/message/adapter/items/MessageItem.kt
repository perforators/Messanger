package com.krivochkov.homework_2.presentation.message.adapter.items

import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.message.adapter.MessageAdapter.Companion.TYPE_MESSAGE
import com.krivochkov.homework_2.domain.models.Message

class MessageItem(val message: Message) : Item {

    override fun areItemsTheSame(otherItem: Item) =
        otherItem is MessageItem && message.id == otherItem.message.id

    override fun areContentsTheSame(otherItem: Item) =
        otherItem is MessageItem && message == otherItem.message

    override fun getType() = TYPE_MESSAGE
}
package com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.items

import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.domain.models.Message

class MessageItem(val message: Message) : Item {

    override fun areItemsTheSame(otherItem: Item) =
        otherItem is MessageItem && message.id == otherItem.message.id

    override fun areContentsTheSame(otherItem: Item) =
        otherItem is MessageItem && message == otherItem.message

    override fun getType() = TYPE

    companion object {
        const val TYPE = 3
    }
}
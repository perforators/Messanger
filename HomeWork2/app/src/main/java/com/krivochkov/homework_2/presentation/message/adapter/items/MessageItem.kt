package com.krivochkov.homework_2.presentation.message.adapter.items

import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.presentation.message.adapter.view_holders.MessageViewHolder.Companion.TYPE_MESSAGE

class MessageItem(val message: Message) : Item {

    override fun areItemsTheSame(otherItem: Item) =
        otherItem is MessageItem && message.id == otherItem.message.id

    override fun areContentsTheSame(otherItem: Item) =
        otherItem is MessageItem && message == otherItem.message

    override fun getType() = TYPE_MESSAGE
}
package com.krivochkov.homework_2.presentation.chat.mappers

import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.items.HeaderTopicItem
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.items.DateSeparatorItem
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.items.MessageItem
import com.krivochkov.homework_2.utils.convertToDate

private const val SECONDS_IN_DAY = 86400

fun List<Message>.toMessageItemsWithDates(): List<Item> {
    val listItems = mutableListOf<Item>()
    val groupedMessages = groupBy { (it.date / SECONDS_IN_DAY) * SECONDS_IN_DAY }

    for (group in groupedMessages) {
        val date = group.key.convertToDate()
        val messageItems = group.value.map { MessageItem(it) }
        listItems += DateSeparatorItem(date)
        listItems += messageItems
    }

    return listItems
}

fun List<Message>.toMessageItemsWithDatesAndTopics(): List<Item> {
    val listItems = mutableListOf<Item>()
    val groupedMessagesByTime = groupBy { (it.date / SECONDS_IN_DAY) * SECONDS_IN_DAY }

    groupedMessagesByTime.forEach { timeGroup ->
        listItems += DateSeparatorItem(timeGroup.key.convertToDate())
        var lastTopic: String? = null
        timeGroup.value.forEach { message ->
            if (lastTopic == null || (lastTopic != null && message.topic != lastTopic)) {
                lastTopic = message.topic
                listItems += HeaderTopicItem(Topic(lastTopic!!))
                listItems += MessageItem(message)
            } else {
                listItems += MessageItem(message)
            }
        }
    }

    return listItems
}
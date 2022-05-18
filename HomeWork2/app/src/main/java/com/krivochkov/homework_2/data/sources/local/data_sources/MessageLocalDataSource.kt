package com.krivochkov.homework_2.data.sources.local.data_sources

import com.krivochkov.homework_2.data.sources.local.entity.MessageEntity
import io.reactivex.Single

interface MessageLocalDataSource {

    fun getAllMessagesFromTopic(channelName: String, topicName: String): Single<List<MessageEntity>>

    fun getAllMessagesFromChannel(channelName: String): Single<List<MessageEntity>>

    fun updateMessagesInTopic(
        channelName: String,
        topicName: String,
        newMessages: List<MessageEntity>
    )
}
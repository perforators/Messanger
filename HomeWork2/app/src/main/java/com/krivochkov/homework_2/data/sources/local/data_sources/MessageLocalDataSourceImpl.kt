package com.krivochkov.homework_2.data.sources.local.data_sources

import com.krivochkov.homework_2.data.sources.local.dao.MessageDao
import com.krivochkov.homework_2.data.sources.local.entity.MessageEntity
import io.reactivex.Single
import javax.inject.Inject

class MessageLocalDataSourceImpl @Inject constructor(
    private val dao: MessageDao
) : MessageLocalDataSource {

    override fun getAllMessagesFromTopic(channelName: String, topicName: String): Single<List<MessageEntity>> {
        return dao.getAllMessagesFromTopic(channelName, topicName)
    }

    override fun getAllMessagesFromChannel(channelName: String): Single<List<MessageEntity>> {
        return dao.getAllMessagesFromChannel(channelName)
    }

    override fun updateMessagesInTopic(
        channelName: String,
        topicName: String,
        newMessages: List<MessageEntity>
    ) {
        dao.updateMessagesInTopic(channelName, topicName, newMessages)
    }
}
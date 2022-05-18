package com.krivochkov.homework_2.data.sources.local.data_sources

import com.krivochkov.homework_2.data.sources.local.dao.MessageDao
import com.krivochkov.homework_2.data.sources.local.entity.MessageEntity
import io.reactivex.Single
import javax.inject.Inject

class MessageLocalDataSourceImpl @Inject constructor(
    private val dao: MessageDao
) : MessageLocalDataSource {

    override fun getMessagesFromTopic(channelName: String, topicName: String): Single<List<MessageEntity>> {
        return dao.getMessagesFromTopic(channelName, topicName)
    }

    override fun getMessagesFromChannel(channelName: String): Single<List<MessageEntity>> {
        return dao.getMessagesFromChannel(channelName)
    }

    override fun refreshMessages(
        channelName: String,
        topicName: String,
        messages: List<MessageEntity>
    ) {
        dao.refreshMessages(channelName, topicName, messages)
    }
}
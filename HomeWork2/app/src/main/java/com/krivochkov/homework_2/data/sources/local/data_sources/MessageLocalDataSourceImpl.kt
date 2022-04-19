package com.krivochkov.homework_2.data.sources.local.data_sources

import com.krivochkov.homework_2.data.sources.local.dao.MessageDao
import com.krivochkov.homework_2.data.sources.local.database.DatabaseInstance
import com.krivochkov.homework_2.data.sources.local.entity.MessageEntity
import io.reactivex.Single

class MessageLocalDataSourceImpl(
    private val dao: MessageDao = DatabaseInstance.instance!!.messageDao()
) : MessageLocalDataSource {

    override fun getMessages(channelName: String, topicName: String): Single<List<MessageEntity>> {
        return dao.getMessages(channelName, topicName)
    }

    override fun refreshMessages(
        channelName: String,
        topicName: String,
        messages: List<MessageEntity>
    ) {
        dao.refreshMessages(channelName, topicName, messages)
    }
}
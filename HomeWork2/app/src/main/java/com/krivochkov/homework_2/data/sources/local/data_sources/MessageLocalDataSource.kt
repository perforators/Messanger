package com.krivochkov.homework_2.data.sources.local.data_sources

import com.krivochkov.homework_2.data.sources.local.entity.MessageEntity
import io.reactivex.Single

interface MessageLocalDataSource {

    fun getMessages(channelName: String, topicName: String): Single<List<MessageEntity>>

    fun refreshMessages(channelName: String, topicName: String, messages: List<MessageEntity>)
}
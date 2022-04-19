package com.krivochkov.homework_2.data.sources.local.data_sources

import com.krivochkov.homework_2.data.sources.local.entity.TopicEntity
import io.reactivex.Single

interface TopicLocalDataSource {

    fun getTopics(channelId: Long): Single<List<TopicEntity>>

    fun refreshTopicsByChannelId(channelId: Long, newTopics: List<TopicEntity>)
}
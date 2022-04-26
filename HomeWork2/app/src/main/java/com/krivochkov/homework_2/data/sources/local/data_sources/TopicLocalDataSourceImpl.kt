package com.krivochkov.homework_2.data.sources.local.data_sources

import com.krivochkov.homework_2.data.sources.local.dao.TopicDao
import com.krivochkov.homework_2.data.sources.local.entity.TopicEntity
import io.reactivex.Single
import javax.inject.Inject

class TopicLocalDataSourceImpl @Inject constructor(
    private val dao: TopicDao
) : TopicLocalDataSource {

    override fun getTopics(channelId: Long): Single<List<TopicEntity>> {
        return dao.getTopics(channelId)
    }

    override fun refreshTopicsByChannelId(channelId: Long, newTopics: List<TopicEntity>) {
        dao.refreshTopics(channelId, newTopics)
    }
}
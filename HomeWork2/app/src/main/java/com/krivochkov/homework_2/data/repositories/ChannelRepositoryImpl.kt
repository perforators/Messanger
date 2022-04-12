package com.krivochkov.homework_2.data.repositories

import com.krivochkov.homework_2.data.mappers.mapToChannel
import com.krivochkov.homework_2.data.mappers.mapToChannelEntity
import com.krivochkov.homework_2.data.mappers.mapToTopic
import com.krivochkov.homework_2.data.mappers.mapToTopicEntity
import com.krivochkov.homework_2.data.sources.local.data_sources.ChannelLocalDataSource
import com.krivochkov.homework_2.data.sources.local.data_sources.ChannelLocalDataSourceImpl
import com.krivochkov.homework_2.data.sources.local.data_sources.TopicLocalDataSource
import com.krivochkov.homework_2.data.sources.local.data_sources.TopicLocalDataSourceImpl
import com.krivochkov.homework_2.data.sources.remote.data_sources.ChannelRemoteDataSource
import com.krivochkov.homework_2.data.sources.remote.data_sources.ChannelRemoteDataSourceImpl
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.domain.repositories.ChannelRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ChannelRepositoryImpl(
    private val channelRemoteDataSource: ChannelRemoteDataSource = ChannelRemoteDataSourceImpl(),
    private val channelLocalDataSource: ChannelLocalDataSource = ChannelLocalDataSourceImpl(),
    private val topicLocalDataSource: TopicLocalDataSource = TopicLocalDataSourceImpl()
) : ChannelRepository {

    override fun getAllChannels(): Single<List<Channel>> {
        val allChannels = channelRemoteDataSource.getAllChannels()
            .subscribeOn(Schedulers.io())

        val subscribedChannels = channelRemoteDataSource.getSubscribedChannels()
            .subscribeOn(Schedulers.io())

        return allChannels.zipWith(subscribedChannels) { all, subscribed ->
            val notSubscribed = all.toMutableList().apply { removeAll(subscribed) }

            channelLocalDataSource.refreshChannelsByCategory(
                false,
                notSubscribed.map { channelDto -> channelDto.mapToChannelEntity(false) },
            )

            channelLocalDataSource.refreshChannelsByCategory(
                true,
                subscribed.map { channelDto -> channelDto.mapToChannelEntity(true) },
            )

            all.map { it.mapToChannel() }
        }
    }

    override fun getSubscribedChannels(): Single<List<Channel>> {
        return channelRemoteDataSource.getSubscribedChannels()
            .map {
                channelLocalDataSource.refreshChannelsByCategory(
                    true,
                    it.map { channelDto -> channelDto.mapToChannelEntity(true) }
                )
                it.map { channelDto -> channelDto.mapToChannel() }
            }
    }

    override fun getTopics(channelId: Long): Single<List<Topic>> {
        return channelRemoteDataSource.getTopicsInChannel(channelId)
            .map {
                topicLocalDataSource.refreshTopicsByChannelId(
                    channelId,
                    it.map { topicDto -> topicDto.mapToTopicEntity(channelId) }
                )
                it.map { topicDto -> topicDto.mapToTopic() }
            }
    }

    override fun getCachedTopics(channelId: Long): Single<List<Topic>> {
        return topicLocalDataSource.getTopics(channelId)
            .map { it.map { topicEntity -> topicEntity.mapToTopic() } }
    }

    override fun getCachedAllChannels(): Single<List<Channel>> {
        return channelLocalDataSource.getAllChannels()
            .map { it.map { channelEntity -> channelEntity.mapToChannel() } }
    }

    override fun getCachedSubscribedChannels(): Single<List<Channel>> {
        return channelLocalDataSource.getSubscribedChannels()
            .map { it.map { channelEntity -> channelEntity.mapToChannel() } }
    }
}
package com.krivochkov.homework_2.data.repositories

import com.krivochkov.homework_2.data.mappers.mapToChannel
import com.krivochkov.homework_2.data.mappers.mapToChannelEntity
import com.krivochkov.homework_2.data.mappers.mapToTopic
import com.krivochkov.homework_2.data.mappers.mapToTopicEntity
import com.krivochkov.homework_2.data.sources.local.data_sources.ChannelLocalDataSource
import com.krivochkov.homework_2.data.sources.local.data_sources.TopicLocalDataSource
import com.krivochkov.homework_2.data.sources.remote.data_sources.ChannelRemoteDataSource
import com.krivochkov.homework_2.data.sources.remote.dto.SubscriptionDto
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.domain.repositories.ChannelRepository
import io.reactivex.Completable
import io.reactivex.Single
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import javax.inject.Inject

class ChannelRepositoryImpl @Inject constructor(
    private val channelRemoteDataSource: ChannelRemoteDataSource,
    private val channelLocalDataSource: ChannelLocalDataSource,
    private val topicLocalDataSource: TopicLocalDataSource
) : ChannelRepository {

    override fun getAllChannels(): Single<List<Channel>> {
        val allChannels = channelRemoteDataSource.getAllChannels()
        val subscribedChannels = channelRemoteDataSource.getSubscribedChannels()

        return allChannels.zipWith(subscribedChannels) { all, subscribed ->
            channelLocalDataSource.updateAllChannels(
                all.map { channelDto ->
                    channelDto.mapToChannelEntity(subscribed.any { channelDto.id == it.id })
                }
            )
            all.map { it.mapToChannel() }
        }
    }

    override fun getSubscribedChannels(): Single<List<Channel>> {
        return channelRemoteDataSource.getSubscribedChannels()
            .map {
                channelLocalDataSource.updateSubscribedChannels(
                    it.map { channelDto -> channelDto.mapToChannelEntity(isSubscribed = true) }
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

    override fun getChannelId(channelName: String): Single<Long> {
        return channelRemoteDataSource.getChannelId(channelName)
            .onErrorReturn { error ->
                if (error is HttpException && error.code() == BAD_REQUEST_CODE) {
                    -1
                } else {
                    throw error
                }
            }
    }

    override fun subscribeToChannel(channelName: String, description: String): Completable {
        val subscription = listOf(SubscriptionDto(channelName, description))
        return channelRemoteDataSource.subscribeToChannels(Json.encodeToString(subscription))
    }

    override fun getCachedAllChannels(): Single<List<Channel>> {
        return channelLocalDataSource.getAllChannels()
            .map { it.map { channelEntity -> channelEntity.mapToChannel() } }
    }

    override fun getCachedSubscribedChannels(): Single<List<Channel>> {
        return channelLocalDataSource.getSubscribedChannels()
            .map { it.map { channelEntity -> channelEntity.mapToChannel() } }
    }

    companion object {

        private const val BAD_REQUEST_CODE = 400
    }
}
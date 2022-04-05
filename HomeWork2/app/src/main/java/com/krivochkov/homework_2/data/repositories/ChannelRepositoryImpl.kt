package com.krivochkov.homework_2.data.repositories

import com.krivochkov.homework_2.data.sources.remote.data_sources.ChannelRemoteDataSource
import com.krivochkov.homework_2.data.sources.remote.data_sources.ChannelRemoteDataSourceImpl
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.domain.repositories.ChannelRepository
import io.reactivex.Single

class ChannelRepositoryImpl(
    private val channelRemoteDataSource: ChannelRemoteDataSource = ChannelRemoteDataSourceImpl(),
) : ChannelRepository {

    override fun loadAllChannels(): Single<List<Channel>> {
        return channelRemoteDataSource.getAllChannels()
            .map { channelsDto -> channelsDto.map { it.toChannel() } }
    }

    override fun loadSubscribedChannels(): Single<List<Channel>> {
        return channelRemoteDataSource.getSubscribedChannels()
            .map { channelsDto -> channelsDto.map { it.toChannel() } }
    }

    override fun loadTopicsInChannel(channelId: Long): Single<List<Topic>> {
        return channelRemoteDataSource.getTopicsInChannel(channelId)
            .map { topicsDto -> topicsDto.map { it.toTopic() } }
    }
}
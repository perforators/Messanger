package com.krivochkov.homework_2.data.sources.remote.data_sources

import com.krivochkov.homework_2.data.sources.remote.api.ZulipApi
import com.krivochkov.homework_2.data.sources.remote.dto.ChannelDto
import com.krivochkov.homework_2.data.sources.remote.dto.TopicDto
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class ChannelRemoteDataSourceImpl @Inject constructor(
    private val api: ZulipApi
) : ChannelRemoteDataSource {

    override fun getAllChannels(): Single<List<ChannelDto>> {
        return api.getAllChannels().map { it.channels }
    }

    override fun getSubscribedChannels(): Single<List<ChannelDto>> {
        return api.getSubscribedChannels().map { it.channels }
    }

    override fun getTopicsInChannel(channelId: Long): Single<List<TopicDto>> {
        return api.getTopicsInChannel(channelId).map { it.topics }
    }

    override fun getChannelId(channelName: String): Single<Long> {
        return api.getChannelId(channelName).map { it.channelId }
    }

    override fun subscribeToChannels(subscriptions: String): Completable {
        return api.subscribeToChannels(subscriptions)
    }
}
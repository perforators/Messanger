package com.krivochkov.homework_2.data.sources.remote.data_sources

import com.krivochkov.homework_2.data.sources.remote.api.ZulipApi
import com.krivochkov.homework_2.data.sources.remote.api.ZulipApiProvider
import com.krivochkov.homework_2.data.sources.remote.dto.ChannelDto
import com.krivochkov.homework_2.data.sources.remote.dto.TopicDto
import io.reactivex.Single

class ChannelRemoteDataSourceImpl(
    private val api: ZulipApi = ZulipApiProvider.zulipApi
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
}
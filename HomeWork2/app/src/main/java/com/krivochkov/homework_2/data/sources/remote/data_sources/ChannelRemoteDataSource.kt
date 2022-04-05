package com.krivochkov.homework_2.data.sources.remote.data_sources

import com.krivochkov.homework_2.data.sources.remote.dto.ChannelDto
import com.krivochkov.homework_2.data.sources.remote.dto.TopicDto
import io.reactivex.Single

interface ChannelRemoteDataSource {

    fun getAllChannels(): Single<List<ChannelDto>>

    fun getSubscribedChannels(): Single<List<ChannelDto>>

    fun getTopicsInChannel(channelId: Long): Single<List<TopicDto>>
}
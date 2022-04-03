package com.krivochkov.homework_2.domain.repositories

import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import io.reactivex.Single

interface ChannelRepository {

    fun loadAllChannels(): Single<List<Channel>>

    fun loadSubscribedChannels(): Single<List<Channel>>

    fun loadTopicsInChannel(channelId: Long): Single<List<Topic>>
}
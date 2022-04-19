package com.krivochkov.homework_2.domain.repositories

import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import io.reactivex.Single

interface ChannelRepository {

    fun getAllChannels(): Single<List<Channel>>

    fun getCachedAllChannels(): Single<List<Channel>>

    fun getSubscribedChannels(): Single<List<Channel>>

    fun getCachedSubscribedChannels(): Single<List<Channel>>

    fun getTopics(channelId: Long): Single<List<Topic>>

    fun getCachedTopics(channelId: Long): Single<List<Topic>>
}
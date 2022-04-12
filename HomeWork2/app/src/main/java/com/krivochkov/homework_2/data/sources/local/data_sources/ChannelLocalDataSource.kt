package com.krivochkov.homework_2.data.sources.local.data_sources

import com.krivochkov.homework_2.data.sources.local.entity.ChannelEntity
import io.reactivex.Single

interface ChannelLocalDataSource {

    fun getAllChannels(): Single<List<ChannelEntity>>

    fun getSubscribedChannels(): Single<List<ChannelEntity>>

    fun refreshChannelsByCategory(subscribed: Boolean, channels: List<ChannelEntity>)
}
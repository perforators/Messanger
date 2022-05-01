package com.krivochkov.homework_2.data.sources.local.data_sources

import com.krivochkov.homework_2.data.sources.local.dao.ChannelDao
import com.krivochkov.homework_2.data.sources.local.entity.ChannelEntity
import io.reactivex.Single
import javax.inject.Inject

class ChannelLocalDataSourceImpl @Inject constructor(
    private val dao: ChannelDao
) : ChannelLocalDataSource {

    override fun getAllChannels(): Single<List<ChannelEntity>> {
        return dao.getAllChannels()
    }

    override fun getSubscribedChannels(): Single<List<ChannelEntity>> {
        return dao.getSubscribedChannels()
    }

    override fun refreshChannelsByCategory(subscribed: Boolean, channels: List<ChannelEntity>) {
        dao.refreshChannels(subscribed, channels)
    }
}
package com.krivochkov.homework_2.domain.repositories

import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic

interface ChannelRepository {

    fun loadAllChannels(): List<Channel>

    fun loadSubscribedChannels(): List<Channel>

    fun loadTopicsInChannel(channelId: Long): List<Topic>
}
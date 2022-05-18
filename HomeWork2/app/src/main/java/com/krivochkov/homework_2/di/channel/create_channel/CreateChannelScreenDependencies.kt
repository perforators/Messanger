package com.krivochkov.homework_2.di.channel.create_channel

import com.krivochkov.homework_2.domain.repositories.ChannelRepository

interface CreateChannelScreenDependencies {

    fun getChannelRepository(): ChannelRepository
}
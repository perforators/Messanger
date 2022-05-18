package com.krivochkov.homework_2.di.topic_pick

import com.krivochkov.homework_2.domain.repositories.ChannelRepository

interface TopicPickScreenDependencies {

    fun getChannelRepository(): ChannelRepository
}
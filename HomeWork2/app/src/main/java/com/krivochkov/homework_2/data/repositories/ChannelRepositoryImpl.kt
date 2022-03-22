package com.krivochkov.homework_2.data.repositories

import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.domain.repositories.ChannelRepository

class ChannelRepositoryImpl : ChannelRepository {

    private val topics = mutableMapOf(
        0L to listOf(
            Topic(1, "Testing", 500),
            Topic(2,"Programming", 14)
        ),
        1L to listOf(
            Topic(3,"Politic", 1023)
        ),
        2L to listOf(
            Topic(4,"jetbrains blog", 23123)
        )
    )

    private val channels = mutableListOf(
        Channel(
            id = 0L,
            name = "#general",
            description = ""
        ),
        Channel(
            id = 1L,
            name = "#news",
            description = ""
        ),
        Channel(
            id = 2L,
            name = "#development",
            description = ""
        )
    )

    override fun loadAllChannels(): List<Channel> {
        return channels.map { it.copy() }
    }

    override fun loadSubscribedChannels(): List<Channel> {
        return channels.subList(0, 2).map { it.copy() }
    }

    override fun loadTopicsInChannel(channelId: Long): List<Topic> {
        val topics = topics[channelId] ?: listOf()
        return topics.map { it.copy() }
    }
}
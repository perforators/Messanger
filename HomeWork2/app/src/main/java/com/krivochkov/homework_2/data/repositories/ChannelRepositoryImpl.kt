package com.krivochkov.homework_2.data.repositories

import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.domain.repositories.ChannelRepository
import io.reactivex.Single
import java.lang.IllegalStateException

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

    override fun loadAllChannels(): Single<List<Channel>> {
        return Single.fromCallable {
            randomException()
            channels.map { it.copy() }
        }
    }

    override fun loadSubscribedChannels(): Single<List<Channel>> {
        return Single.fromCallable {
            randomException()
            channels.subList(0, 2).map { it.copy() }
        }
    }

    override fun loadTopicsInChannel(channelId: Long): Single<List<Topic>> {
        return Single.fromCallable {
            randomException()
            val topics = topics[channelId] ?: listOf()
            topics.map { it.copy() }
        }
    }

    private fun randomException() {
        if ((1..10).random() < 4) throw IllegalStateException("Random error")
    }
}
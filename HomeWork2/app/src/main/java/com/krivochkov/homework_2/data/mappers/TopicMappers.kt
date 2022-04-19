package com.krivochkov.homework_2.data.mappers

import com.krivochkov.homework_2.data.sources.local.entity.TopicEntity
import com.krivochkov.homework_2.data.sources.remote.dto.TopicDto
import com.krivochkov.homework_2.domain.models.Topic

fun TopicDto.mapToTopic() = Topic(name)

fun TopicEntity.mapToTopic() = Topic(name)

fun TopicDto.mapToTopicEntity(channelId: Long) = TopicEntity(
    name = name,
    channelId = channelId
)
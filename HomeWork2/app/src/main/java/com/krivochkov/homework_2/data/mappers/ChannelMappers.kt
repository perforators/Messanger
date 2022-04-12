package com.krivochkov.homework_2.data.mappers

import com.krivochkov.homework_2.data.sources.local.entity.ChannelEntity
import com.krivochkov.homework_2.data.sources.remote.dto.ChannelDto
import com.krivochkov.homework_2.domain.models.Channel

fun ChannelDto.mapToChannel() = Channel(
    id = id,
    name = name,
    description = description
)

fun ChannelEntity.mapToChannel() = Channel(
    id = id,
    name = name,
    description = description
)

fun ChannelDto.mapToChannelEntity(isSubscribed: Boolean) = ChannelEntity(
    id = id,
    name = name,
    isSubscribed = isSubscribed,
    description = description
)
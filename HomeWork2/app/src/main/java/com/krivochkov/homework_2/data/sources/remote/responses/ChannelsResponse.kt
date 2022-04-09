package com.krivochkov.homework_2.data.sources.remote.responses

import com.krivochkov.homework_2.data.sources.remote.dto.ChannelDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllChannelsResponse(
    @SerialName("streams") val channels: List<ChannelDto>
)

@Serializable
data class SubscribedChannelsResponse(
    @SerialName("subscriptions") val channels: List<ChannelDto>
)
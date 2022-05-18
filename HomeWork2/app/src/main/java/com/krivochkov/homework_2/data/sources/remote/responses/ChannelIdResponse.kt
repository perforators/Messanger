package com.krivochkov.homework_2.data.sources.remote.responses

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelIdResponse(
    @SerialName("stream_id") val channelId: Long
)
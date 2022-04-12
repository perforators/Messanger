package com.krivochkov.homework_2.data.sources.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelDto(
    @SerialName("stream_id") val id: Long,
    @SerialName("description") val description: String,
    @SerialName("name") val name: String
)
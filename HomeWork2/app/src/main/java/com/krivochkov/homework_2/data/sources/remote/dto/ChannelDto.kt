package com.krivochkov.homework_2.data.sources.remote.dto

import com.krivochkov.homework_2.domain.models.Channel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelDto(
    @SerialName("stream_id") val id: Long,
    @SerialName("description") val description: String,
    @SerialName("name") val name: String
) {

    fun toChannel() = Channel(
        id = id,
        name = name,
        description = description
    )
}
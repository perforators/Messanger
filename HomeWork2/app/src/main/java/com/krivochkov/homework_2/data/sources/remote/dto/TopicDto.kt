package com.krivochkov.homework_2.data.sources.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicDto(
    @SerialName("name") val name: String,
)
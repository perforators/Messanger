package com.krivochkov.homework_2.data.sources.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresenceDto(
    @SerialName("status") val status: String,
    @SerialName("timestamp") val timestamp: Long
)
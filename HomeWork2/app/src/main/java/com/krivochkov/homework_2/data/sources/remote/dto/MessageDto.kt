package com.krivochkov.homework_2.data.sources.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageDto(
    @SerialName("avatar_url") val avatar: String? = null,
    @SerialName("id") val id: Long,
    @SerialName("content") val content: String,
    @SerialName("sender_full_name") val senderName: String,
    @SerialName("sender_id") val senderId: Long,
    @SerialName("timestamp") val timestamp: Long,
    @SerialName("reactions") val reactions: List<ReactionDto>,
    @SerialName("subject") val topic: String
)
package com.krivochkov.homework_2.data.sources.remote.responses

import com.krivochkov.homework_2.data.sources.remote.dto.MessageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessagesResponse(
    @SerialName("messages") val messages: List<MessageDto>
)

@Serializable
data class SingleMessageResponse(
    @SerialName("message") val message: MessageDto
)
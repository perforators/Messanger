package com.krivochkov.homework_2.data.sources.remote.responses

import com.krivochkov.homework_2.data.sources.remote.dto.TopicDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicsResponse(
    @SerialName("topics") val topics: List<TopicDto>
)
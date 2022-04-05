package com.krivochkov.homework_2.data.sources.remote.dto

import com.krivochkov.homework_2.domain.models.Topic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopicDto(
    @SerialName("name") val name: String,
) {

    fun toTopic() = Topic(name)
}
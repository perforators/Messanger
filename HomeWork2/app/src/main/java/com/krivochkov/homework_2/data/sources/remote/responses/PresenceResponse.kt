package com.krivochkov.homework_2.data.sources.remote.responses

import com.krivochkov.homework_2.data.sources.remote.dto.PresenceDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PresenceResponse(
    @SerialName("presence") val aggregatedPresence: AggregatedPresence
)

@Serializable
data class AggregatedPresence(
    @SerialName("aggregated") val presence: PresenceDto
)
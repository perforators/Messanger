package com.krivochkov.homework_2.data.sources.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NarrowDto(
    @SerialName("operator") val operator: String,
    @SerialName("operand") val operand: String
) {

    companion object {
        const val OPERATOR_CHANNEL = "stream"
        const val OPERATOR_TOPIC = "topic"
    }
}
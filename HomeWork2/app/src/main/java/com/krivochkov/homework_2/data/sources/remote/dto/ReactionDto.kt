package com.krivochkov.homework_2.data.sources.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReactionDto(
    @SerialName("emoji_code") val emojiCode: String,
    @SerialName("emoji_name") val emojiName: String,
    @SerialName("reaction_type") val type: String,
    @SerialName("user_id") val userId: Long
) {

    companion object {
        const val UNICODE_REACTION_TYPE = "unicode_emoji"
    }
}
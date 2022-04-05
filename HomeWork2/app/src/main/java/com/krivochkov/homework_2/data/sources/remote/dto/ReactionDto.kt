package com.krivochkov.homework_2.data.sources.remote.dto

import com.krivochkov.homework_2.domain.models.Emoji
import com.krivochkov.homework_2.domain.models.Reaction
import com.krivochkov.homework_2.utils.convertEmojiToUtf
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReactionDto(
    @SerialName("emoji_code") val emojiCode: String,
    @SerialName("emoji_name") val emojiName: String,
    @SerialName("reaction_type") val type: String,
    @SerialName("user_id") val userId: Long
) {

    fun toReaction() = Reaction(
        userId = userId,
        emoji = Emoji(emojiName, emojiCode.convertEmojiToUtf())
    )
}
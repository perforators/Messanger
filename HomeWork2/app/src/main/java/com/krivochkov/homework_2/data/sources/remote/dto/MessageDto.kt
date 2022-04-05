package com.krivochkov.homework_2.data.sources.remote.dto

import android.text.Html
import com.krivochkov.homework_2.domain.models.GroupedReaction
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.domain.models.Reaction
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
    @SerialName("reactions") val reactions: List<ReactionDto>
) {

    fun toMessage(myUserId: Long): Message {
        val reactions = reactions.map { it.toReaction() }
        return Message(
            id = id,
            userName = senderName,
            avatarUrl = avatar,
            isMyMessage = myUserId == senderId,
            text = Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT).toString().trim(),
            date = timestamp,
            reactions = reactions,
            groupedReactions = groupReaction(reactions, myUserId)
        )
    }

    private fun groupReaction(reactions: List<Reaction>, myUserId: Long): List<GroupedReaction> {
        val groupedReactions = mutableListOf<GroupedReaction>()
        val groupedReactionsByEmoji = reactions.groupBy { it.emoji }
        for (reactionGroup in groupedReactionsByEmoji) {
            val emoji = reactionGroup.key
            val reactionsCount = reactionGroup.value.size
            val isSelected = reactionGroup.value.any { it.userId == myUserId }
            groupedReactions.add(GroupedReaction(emoji, reactionsCount, isSelected))
        }
        return groupedReactions
    }
}
package com.krivochkov.homework_2.data.mappers

import android.text.Html
import com.krivochkov.homework_2.data.sources.local.entity.MessageEntity
import com.krivochkov.homework_2.data.sources.remote.dto.MessageDto
import com.krivochkov.homework_2.data.sources.remote.dto.ReactionDto
import com.krivochkov.homework_2.domain.models.GroupedReaction
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.domain.models.Reaction

fun MessageDto.mapToMessage(myUserId: Long): Message {
    val reactions = reactions.filter { it.type == ReactionDto.UNICODE_REACTION_TYPE }
        .map { it.mapToReaction() }

    return Message(
        id = id,
        userName = senderName,
        avatarUrl = avatar,
        isMyMessage = myUserId == senderId,
        text = Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT).toString().trim(),
        date = timestamp,
        topic = topic,
        reactions = reactions,
        groupedReactions = groupReaction(reactions, myUserId)
    )
}

fun MessageEntity.mapToMessage() = Message(
    id = id,
    userName = userName,
    avatarUrl = avatar,
    isMyMessage = isMyMessage,
    text = text,
    date = date,
    topic = topicName,
    reactions = reactions,
    groupedReactions = groupedReactions
)

fun Message.mapToMessageEntity(channelName: String) = MessageEntity(
    id = id,
    channelName = channelName,
    topicName = topic,
    userName = userName,
    avatar = avatarUrl,
    isMyMessage = isMyMessage,
    text = text,
    date = date,
    reactions = reactions,
    groupedReactions = groupedReactions
)

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
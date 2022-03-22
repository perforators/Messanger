package com.krivochkov.homework_2.domain.models

import com.krivochkov.homework_2.data.repositories.MessageRepositoryImpl

data class Message(
    val id: Long,
    val userId: Int,
    val userName: String,
    val avatarUrl: String,
    val isMeMessage: Boolean,
    val text: String,
    val date: Long,
    val reactions: MutableList<Reaction>,
) {
    val groupedReactions: List<GroupedReaction> by lazy { groupReaction() }

    private fun groupReaction(): List<GroupedReaction> {
        val groupedReactions = mutableListOf<GroupedReaction>()
        val groupedReactionsByEmoji = reactions.groupBy { it.emoji }
        for (reactionGroup in groupedReactionsByEmoji) {
            val emoji = reactionGroup.key
            val reactionsCount = reactionGroup.value.size
            val isSelected = reactionGroup.value.any { it.userId == MessageRepositoryImpl.MY_USER_ID }
            groupedReactions.add(GroupedReaction(emoji, reactionsCount, isSelected))
        }
        return groupedReactions
    }
}
package com.krivochkov.homework_2.models

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
    val groupedReactions: Map<String, List<Reaction>>
        get() = reactions.groupBy { it.emoji }
}
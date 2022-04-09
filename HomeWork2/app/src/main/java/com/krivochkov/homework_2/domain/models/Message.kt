package com.krivochkov.homework_2.domain.models

data class Message(
    val id: Long,
    val userName: String,
    val avatarUrl: String?,
    val isMyMessage: Boolean,
    val text: String,
    val date: Long,
    val reactions: List<Reaction>,
    val groupedReactions: List<GroupedReaction>
)
package com.krivochkov.homework_2.domain.models

data class GroupedReaction(
    val emoji: Emoji,
    val reactionsCount: Int,
    val isSelected: Boolean
)
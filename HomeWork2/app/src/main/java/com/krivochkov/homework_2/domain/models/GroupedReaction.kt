package com.krivochkov.homework_2.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class GroupedReaction(
    val emoji: Emoji,
    val reactionsCount: Int,
    val isSelected: Boolean
)
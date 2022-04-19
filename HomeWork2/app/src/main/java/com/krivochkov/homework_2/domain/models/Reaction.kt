package com.krivochkov.homework_2.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Reaction(val userId: Long, val emoji: Emoji)
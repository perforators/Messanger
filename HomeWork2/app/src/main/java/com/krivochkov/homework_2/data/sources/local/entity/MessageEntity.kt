package com.krivochkov.homework_2.data.sources.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.krivochkov.homework_2.domain.models.GroupedReaction
import com.krivochkov.homework_2.domain.models.Reaction

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey
    val id: Long,
    val channelName: String,
    val topicName: String,
    val avatar: String? = null,
    val userName: String,
    val text: String,
    val isMyMessage: Boolean,
    val date: Long,
    val reactions: List<Reaction>,
    val groupedReactions: List<GroupedReaction>
)
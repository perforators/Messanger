package com.krivochkov.homework_2.data.sources.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "channels")
data class ChannelEntity(
    @PrimaryKey
    val id: Long,
    val description: String,
    val isSubscribed: Boolean,
    val name: String
)
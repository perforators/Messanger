package com.krivochkov.homework_2.data.sources.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Long,
    val avatar: String? = null,
    val email: String,
    val fullName: String,
    val isMe: Boolean
)
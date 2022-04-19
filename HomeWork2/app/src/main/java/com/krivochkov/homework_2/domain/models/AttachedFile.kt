package com.krivochkov.homework_2.domain.models

data class AttachedFile(
    val name: String,
    val type: String,
    val localPath: String,
    val remotePath: String? = null
)
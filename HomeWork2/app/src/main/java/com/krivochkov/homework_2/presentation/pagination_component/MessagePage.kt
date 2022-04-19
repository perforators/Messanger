package com.krivochkov.homework_2.presentation.pagination_component

data class MessagePage(val lastMessageId: Long, val numBefore: Int, val isRetry: Boolean)
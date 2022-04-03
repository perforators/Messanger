package com.krivochkov.homework_2.presentation.message

import com.krivochkov.homework_2.presentation.Item

sealed class ScreenState {
    data class MessagesLoaded(val messagesWithDates: List<Item>) : ScreenState()
    object Loading : ScreenState()
    object Error : ScreenState()
}
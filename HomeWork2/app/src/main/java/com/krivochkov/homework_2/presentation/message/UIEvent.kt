package com.krivochkov.homework_2.presentation.message

sealed class UIEvent {
    object FailedSendMessage : UIEvent()
    object FailedAddReaction: UIEvent()
    object FailedRemoveReaction: UIEvent()
}
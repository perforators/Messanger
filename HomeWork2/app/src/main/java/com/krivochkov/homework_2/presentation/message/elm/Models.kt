package com.krivochkov.homework_2.presentation.message.elm

import com.krivochkov.homework_2.domain.models.AttachedFile
import com.krivochkov.homework_2.domain.models.Emoji
import com.krivochkov.homework_2.domain.models.Message

data class MessageState(
    val channelName: String = "",
    val topicName: String = "",
    val items: List<Any> = emptyList(),
    val areCachedItemsSet: Boolean = false,
    val attachedFiles: List<AttachedFile> = emptyList(),
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val isInitialized: Boolean = false,
    val lastMessageId: Long = 0,
    val pageSize: Int = DEFAULT_PAGE_SIZE
) {

    companion object {
        private const val DEFAULT_PAGE_SIZE = 20
    }
}

sealed class MessageEvent {

    sealed class Ui : MessageEvent() {
        data class Init(val channelName: String, val topicName: String) : Ui()
        object LoadNextPage : Ui()
        object RefreshFirstPage : Ui()
        data class ShowEmojiPicker(val messageId: Long) : Ui()
        object BackButtonClick : Ui()
        object ShowFilePicker : Ui()
        data class AddAttachedFile(val attachedFile: AttachedFile) : Ui()
        data class RemoveAttachedFile(val attachedFile: AttachedFile) : Ui()
        data class SendMessage(val message: String) : Ui()
        data class RefreshMessage(val messageId: Long) : Ui()
        data class AddReaction(val messageId: Long, val emoji: Emoji) : Ui()
        data class RemoveReaction(val messageId: Long, val emoji: Emoji) : Ui()
        data class UpdateReaction(val messageId: Long, val emoji: Emoji) : Ui()
    }

    sealed class Internal : MessageEvent() {
        data class CachedMessagesLoaded(val messages: List<Message>) : Internal()
        data class PageLoaded(val messages: List<Message>, val isFirstPage: Boolean) : Internal()
        data class SingleMessageLoaded(val message: Message) : Internal()
        data class MessageSent(val channelName: String, val topicName: String) : Internal()
        data class ReactionUpdated(val messageId: Long) : Internal()
        data class ErrorLoadingCachedMessages(val error: Throwable) : Internal()
        data class ErrorLoadingPage(val error: Throwable) : Internal()
        data class ErrorSendingMessage(val error: Throwable) : Internal()
        data class ErrorLoadingSingleMessage(val error: Throwable) : Internal()
        data class ErrorAddingReaction(val error: Throwable) : Internal()
        data class ErrorRemovingReaction(val error: Throwable) : Internal()
    }
}

sealed class MessageEffect {
    object CachedMessagesLoadError : MessageEffect()
    object NextPageLoadError : MessageEffect()
    object SendMessageError : MessageEffect()
    object AddReactionError : MessageEffect()
    object RemoveReactionError : MessageEffect()
    object RefreshSingleMessageError : MessageEffect()
    data class ShowEmojiPicker(val messageId: Long) : MessageEffect()
    object ShowFilePicker : MessageEffect()
    object NavigateUp : MessageEffect()
}

sealed class MessageCommand {
    data class LoadCachedMessages(val channelName: String, val topicName: String) : MessageCommand()
    data class LoadSingleMessage(val messageId: Long) : MessageCommand()
    data class AddReaction(val messageId: Long, val emojiName: String) : MessageCommand()
    data class RemoveReaction(val messageId: Long, val emojiName: String) : MessageCommand()

    data class LoadPage(
        val channelName: String,
        val topicName: String,
        val lastMessageId: Long,
        val pageSize: Int
    ) : MessageCommand()

    data class SendMessage(
        val channelName: String,
        val topicName: String,
        val message: String,
        val attachedFiles: List<AttachedFile>
    ) : MessageCommand()
}
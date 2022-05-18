package com.krivochkov.homework_2.presentation.chat.elm

import com.krivochkov.homework_2.domain.models.AttachedFile
import com.krivochkov.homework_2.domain.models.Emoji
import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.domain.models.Topic

data class ChatState(
    val channelName: String = "",
    val topicName: String = "",
    val topicForSendingMessages: String = "",
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

sealed class ChatEvent {

    sealed class Ui : ChatEvent() {
        data class Init(val channelName: String, val topicName: String = "") : Ui()
        object LoadNextPage : Ui()
        object RefreshFirstPage : Ui()
        data class ChangeTopicsForSendingMessages(val topic: Topic) : Ui()
        data class ShowEmojiPicker(val messageId: Long) : Ui()
        data class ShowTopicPicker(val channelId: Long) : Ui()
        object BackButtonClick : Ui()
        object ShowFilePicker : Ui()
        data class ShowTopicChatContent(val topic: Topic) : Ui()
        data class AddAttachedFile(val attachedFile: AttachedFile) : Ui()
        data class RemoveAttachedFile(val attachedFile: AttachedFile) : Ui()
        data class SendMessage(val message: String) : Ui()
        data class RefreshMessage(val messageId: Long) : Ui()
        data class AddReaction(val messageId: Long, val emoji: Emoji) : Ui()
        data class RemoveReaction(val messageId: Long, val emoji: Emoji) : Ui()
        data class UpdateReaction(val messageId: Long, val emoji: Emoji) : Ui()
    }

    sealed class Internal : ChatEvent() {
        data class CachedMessagesLoaded(val messages: List<Message>) : Internal()
        data class PageLoaded(val messages: List<Message>, val isFirstPage: Boolean) : Internal()
        data class SingleMessageLoaded(val message: Message) : Internal()
        object MessageSent : Internal()
        data class ReactionUpdated(val messageId: Long) : Internal()
        data class ErrorLoadingCachedMessages(val error: Throwable) : Internal()
        data class ErrorLoadingPage(val error: Throwable) : Internal()
        data class ErrorSendingMessage(val error: Throwable) : Internal()
        data class ErrorLoadingSingleMessage(val error: Throwable) : Internal()
        data class ErrorAddingReaction(val error: Throwable) : Internal()
        data class ErrorRemovingReaction(val error: Throwable) : Internal()
    }
}

sealed class ChatEffect {
    object CachedMessagesLoadError : ChatEffect()
    object NextPageLoadError : ChatEffect()
    object SendMessageError : ChatEffect()
    object AddReactionError : ChatEffect()
    object RemoveReactionError : ChatEffect()
    object RefreshSingleMessageError : ChatEffect()
    data class ShowEmojiPicker(val messageId: Long) : ChatEffect()
    data class ShowTopicPicker(val channelId: Long) : ChatEffect()
    data class ShowTopicChatContent(val topic: Topic) : ChatEffect()
    object ShowFilePicker : ChatEffect()
    object NavigateUp : ChatEffect()
}

sealed class ChatCommand {
    data class LoadCachedMessages(val channelName: String, val topicName: String) : ChatCommand()
    data class LoadSingleMessage(val messageId: Long) : ChatCommand()
    data class AddReaction(val messageId: Long, val emojiName: String) : ChatCommand()
    data class RemoveReaction(val messageId: Long, val emojiName: String) : ChatCommand()

    data class LoadPage(
        val channelName: String,
        val topicName: String = "",
        val lastMessageId: Long,
        val pageSize: Int
    ) : ChatCommand()

    data class SendMessage(
        val channelName: String,
        val topicName: String,
        val message: String,
        val attachedFiles: List<AttachedFile>
    ) : ChatCommand()
}
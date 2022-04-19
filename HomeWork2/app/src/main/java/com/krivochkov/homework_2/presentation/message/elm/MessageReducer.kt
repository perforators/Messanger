package com.krivochkov.homework_2.presentation.message.elm

import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.LoadingItem
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

class MessageReducer : ScreenDslReducer<MessageEvent, MessageEvent.Ui, MessageEvent.Internal, MessageState, MessageEffect, MessageCommand>(
    MessageEvent.Ui::class,
    MessageEvent.Internal::class
) {

    override fun Result.internal(event: MessageEvent.Internal): Any {
        return when (event) {
            is MessageEvent.Internal.CachedMessagesLoaded -> {
                state {
                    copy(
                        isLoading = event.messages.isEmpty(),
                        error = null,
                        items = event.messages,
                        areCachedItemsSet = true,
                    )
                }
            }
            is MessageEvent.Internal.PageLoaded -> {
                val itemsList = if (event.isFirstPage && state.areCachedItemsSet.not()) {
                    val indexOfOccurrence = state.items.indexOfFirst {
                        it is Message && it.id == event.messages[0].id
                    }

                    if (indexOfOccurrence == -1) {
                        state.items.toMutableList().apply { addAll(this.lastIndex + 1, event.messages) }
                    } else {
                        val updatedMessages = state.items.subList(0, indexOfOccurrence).toMutableList()
                        updatedMessages.apply { addAll(indexOfOccurrence, event.messages) }
                    }
                } else if (event.isFirstPage && state.areCachedItemsSet) {
                    event.messages
                } else {
                    event.messages + state.items.removeLoadingItem()
                }

                val lastMessageId = (itemsList.first { it is Message } as Message).id
                state {
                    copy(
                        items = itemsList,
                        isLoading = false,
                        error = null,
                        lastMessageId = lastMessageId,
                        areCachedItemsSet = false
                    )
                }
            }
            is MessageEvent.Internal.SingleMessageLoaded -> {
                val itemsList = state.items.toMutableList().apply {
                    replaceMessage(event.message.id) { event.message }
                }
                state { copy(items = itemsList) }
            }
            is MessageEvent.Internal.MessageSent -> {
                commands {
                    +MessageCommand.LoadPage(event.channelName, event.topicName, 0, state.pageSize)
                }
            }
            is MessageEvent.Internal.ReactionUpdated -> {
                commands { +MessageCommand.LoadSingleMessage(event.messageId) }
            }
            is MessageEvent.Internal.ErrorLoadingCachedMessages -> {
                effects { +MessageEffect.CachedMessagesLoadError }
            }
            is MessageEvent.Internal.ErrorLoadingPage -> {
                if (state.items.isEmpty()) {
                    state { copy(error = event.error, isLoading = false) }
                } else {
                    state { copy(items = state.items.removeLoadingItem(), isLoading = false) }
                    effects { +MessageEffect.NextPageLoadError }
                }
            }
            is MessageEvent.Internal.ErrorSendingMessage -> {
                effects { +MessageEffect.SendMessageError }
            }
            is MessageEvent.Internal.ErrorLoadingSingleMessage -> {
                effects { +MessageEffect.RefreshSingleMessageError }
            }
            is MessageEvent.Internal.ErrorAddingReaction -> {
                effects { +MessageEffect.AddReactionError }
            }
            is MessageEvent.Internal.ErrorRemovingReaction -> {
                effects { +MessageEffect.RemoveReactionError }
            }
        }
    }

    override fun Result.ui(event: MessageEvent.Ui): Any {
        return when (event) {
            is MessageEvent.Ui.Init -> {
                if (state.isInitialized.not() ||
                    state.channelName != event.channelName || state.topicName != event.topicName) {
                    state {
                        copy(
                            channelName = event.channelName,
                            topicName = event.topicName,
                            items = emptyList(),
                            attachedFiles = emptyList(),
                            areCachedItemsSet = false,
                            isLoading = true,
                            isInitialized = true,
                            error = null,
                            lastMessageId = 0
                        )
                    }
                    commands {
                        +MessageCommand.LoadCachedMessages(state.channelName, state.topicName)
                        +MessageCommand.LoadPage(
                            state.channelName, state.topicName, 0, state.pageSize
                        )
                    }
                } else {
                    Any()
                }
            }
            is MessageEvent.Ui.BackButtonClick -> {
                effects { +MessageEffect.NavigateUp }
            }
            is MessageEvent.Ui.LoadNextPage -> {
                if (state.items.contains(LoadingItem) || state.areCachedItemsSet) {
                    Any()
                } else {
                    state { copy(items = listOf(LoadingItem) + items, error = null, isLoading = false) }
                    commands {
                        +MessageCommand.LoadPage(
                            state.channelName, state.topicName, state.lastMessageId, state.pageSize
                        )
                    }
                }
            }
            is MessageEvent.Ui.RefreshFirstPage -> {
                commands {
                    +MessageCommand.LoadPage(
                        state.channelName, state.topicName, 0, state.pageSize
                    )
                }
            }
            is MessageEvent.Ui.ShowEmojiPicker -> {
                effects { +MessageEffect.ShowEmojiPicker(event.messageId) }
            }
            is MessageEvent.Ui.ShowFilePicker -> {
                effects { +MessageEffect.ShowFilePicker }
            }
            is MessageEvent.Ui.AddAttachedFile -> {
                state { copy(attachedFiles = attachedFiles + event.attachedFile) }
            }
            is MessageEvent.Ui.RemoveAttachedFile -> {
                val attachedFiles = state.attachedFiles.toMutableList().apply {
                    remove(event.attachedFile)
                }
                state { copy(attachedFiles = attachedFiles) }
            }
            is MessageEvent.Ui.SendMessage -> {
                val attachedFiles = state.attachedFiles
                state { copy(attachedFiles = emptyList()) }
                commands {
                    +MessageCommand.SendMessage(
                        state.channelName, state.topicName, event.message, attachedFiles
                    )
                }
            }
            is MessageEvent.Ui.RefreshMessage -> {
                commands { +MessageCommand.LoadSingleMessage(event.messageId) }
            }
            is MessageEvent.Ui.AddReaction -> {
                commands { +MessageCommand.AddReaction(event.messageId, event.emoji.name) }
            }
            is MessageEvent.Ui.RemoveReaction -> {
                commands { +MessageCommand.RemoveReaction(event.messageId, event.emoji.name) }
            }
            is MessageEvent.Ui.UpdateReaction -> {
                val message = state.items.find { it is Message && it.id == event.messageId } as? Message
                val groupedReaction = message?.groupedReactions?.find { it.emoji.name == event.emoji.name }
                if (groupedReaction == null || !groupedReaction.isSelected) {
                    commands { +MessageCommand.AddReaction(event.messageId, event.emoji.name) }
                } else {
                    commands { +MessageCommand.RemoveReaction(event.messageId, event.emoji.name) }
                }
            }
        }
    }

    private fun List<Any>.removeLoadingItem() = filter { it !is LoadingItem }

    private fun MutableList<Any>.replaceMessage(messageId: Long, replacer: (Message) -> Message) {
        replaceAll { item->
            if (item is Message && item.id == messageId)
                replacer(item)
            else
                item
        }
    }
}
package com.krivochkov.homework_2.presentation.chat.elm

import com.krivochkov.homework_2.domain.models.Message
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.LoadingItem
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

class ChatReducer : ScreenDslReducer<ChatEvent, ChatEvent.Ui, ChatEvent.Internal, ChatState, ChatEffect, ChatCommand>(
    ChatEvent.Ui::class,
    ChatEvent.Internal::class
) {

    override fun Result.internal(event: ChatEvent.Internal): Any {
        return when (event) {
            is ChatEvent.Internal.CachedMessagesLoaded -> {
                state {
                    copy(
                        isLoading = event.messages.isEmpty(),
                        error = null,
                        items = event.messages,
                        areCachedItemsSet = true,
                    )
                }
                commands {
                    +ChatCommand.LoadPage(
                        state.channelName, state.topicName, 0, state.pageSize
                    )
                }
            }
            is ChatEvent.Internal.PageLoaded -> {
                val itemsList = if (event.isFirstPage && state.areCachedItemsSet.not()) {
                    val indexOfOccurrence = state.items.indexOfFirst {
                        it is Message && it.id == event.messages[0].id
                    }

                    if (indexOfOccurrence == -1) {
                        state.items.toMutableList().apply {
                            addAll(this.lastIndex + 1, event.messages)
                        }
                    } else {
                        val updatedMessages =
                            state.items.subList(0, indexOfOccurrence).toMutableList()
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
            is ChatEvent.Internal.SingleMessageLoaded -> {
                val itemsList = state.items.toMutableList().apply {
                    replaceMessage(event.message.id) { event.message }
                }
                state { copy(items = itemsList) }
            }
            is ChatEvent.Internal.MessageSent -> {
                commands {
                    +ChatCommand.LoadPage(
                        state.channelName, state.topicName, 0, state.pageSize
                    )
                }
            }
            is ChatEvent.Internal.ReactionUpdated -> {
                commands { +ChatCommand.LoadSingleMessage(event.messageId) }
            }
            is ChatEvent.Internal.ErrorLoadingCachedMessages -> {
                effects { +ChatEffect.CachedMessagesLoadError }
            }
            is ChatEvent.Internal.ErrorLoadingPage -> {
                if (state.items.isEmpty()) {
                    state { copy(error = event.error, isLoading = false) }
                } else {
                    state { copy(items = state.items.removeLoadingItem(), isLoading = false) }
                    effects { +ChatEffect.NextPageLoadError }
                }
            }
            is ChatEvent.Internal.ErrorSendingMessage -> {
                effects { +ChatEffect.SendMessageError }
            }
            is ChatEvent.Internal.ErrorLoadingSingleMessage -> {
                effects { +ChatEffect.RefreshSingleMessageError }
            }
            is ChatEvent.Internal.ErrorAddingReaction -> {
                effects { +ChatEffect.AddReactionError }
            }
            is ChatEvent.Internal.ErrorRemovingReaction -> {
                effects { +ChatEffect.RemoveReactionError }
            }
        }
    }

    override fun Result.ui(event: ChatEvent.Ui): Any {
        return when (event) {
            is ChatEvent.Ui.Init -> {
                if (state.isInitialized.not()) {
                    state {
                        copy(
                            channelName = event.channelName,
                            topicName = event.topicName,
                            topicForSendingMessages = event.topicName,
                            isLoading = true,
                            isInitialized = true,
                            items = emptyList(),
                            attachedFiles = emptyList(),
                            areCachedItemsSet = false,
                            error = null,
                            lastMessageId = 0
                        )
                    }
                    commands {
                        +ChatCommand.LoadCachedMessages(state.channelName, state.topicName)
                    }
                } else {
                    Any()
                }
            }
            is ChatEvent.Ui.ChangeTopicsForSendingMessages -> {
                state { copy(topicForSendingMessages = event.topic.name) }
            }
            is ChatEvent.Ui.ShowTopicChatContent -> {
                effects { +ChatEffect.ShowTopicChatContent(event.topic) }
            }
            is ChatEvent.Ui.ShowTopicPicker -> {
                effects { +ChatEffect.ShowTopicPicker(event.channelId) }
            }
            is ChatEvent.Ui.BackButtonClick -> {
                effects { +ChatEffect.NavigateUp }
            }
            is ChatEvent.Ui.LoadNextPage -> {
                if (state.items.contains(LoadingItem)) {
                    Any()
                } else {
                    state {
                        copy(items = listOf(LoadingItem) + items, error = null, isLoading = false)
                    }
                    commands {
                        +ChatCommand.LoadPage(
                            state.channelName, state.topicName, state.lastMessageId, state.pageSize
                        )
                    }
                }
            }
            is ChatEvent.Ui.RefreshFirstPage -> {
                commands {
                    state { copy(isLoading = items.isEmpty(), error = null) }
                    +ChatCommand.LoadPage(
                        state.channelName, state.topicName, 0, state.pageSize
                    )
                }
            }
            is ChatEvent.Ui.ShowEmojiPicker -> {
                effects { +ChatEffect.ShowEmojiPicker(event.messageId) }
            }
            is ChatEvent.Ui.ShowFilePicker -> {
                effects { +ChatEffect.ShowFilePicker }
            }
            is ChatEvent.Ui.AddAttachedFile -> {
                state { copy(attachedFiles = attachedFiles + event.attachedFile) }
            }
            is ChatEvent.Ui.RemoveAttachedFile -> {
                val attachedFiles = state.attachedFiles.toMutableList().apply {
                    remove(event.attachedFile)
                }
                state { copy(attachedFiles = attachedFiles) }
            }
            is ChatEvent.Ui.SendMessage -> {
                val attachedFiles = state.attachedFiles
                state { copy(attachedFiles = emptyList()) }
                commands {
                    +ChatCommand.SendMessage(
                        state.channelName,
                        state.topicForSendingMessages,
                        event.message,
                        attachedFiles
                    )
                }
            }
            is ChatEvent.Ui.RefreshMessage -> {
                commands { +ChatCommand.LoadSingleMessage(event.messageId) }
            }
            is ChatEvent.Ui.AddReaction -> {
                commands { +ChatCommand.AddReaction(event.messageId, event.emoji.name) }
            }
            is ChatEvent.Ui.RemoveReaction -> {
                commands { +ChatCommand.RemoveReaction(event.messageId, event.emoji.name) }
            }
            is ChatEvent.Ui.UpdateReaction -> {
                val message = state.items.find {
                    it is Message && it.id == event.messageId
                } as? Message
                val groupedReaction = message?.groupedReactions?.find {
                    it.emoji.name == event.emoji.name
                }
                if (groupedReaction == null || !groupedReaction.isSelected) {
                    commands { +ChatCommand.AddReaction(event.messageId, event.emoji.name) }
                } else {
                    commands { +ChatCommand.RemoveReaction(event.messageId, event.emoji.name) }
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
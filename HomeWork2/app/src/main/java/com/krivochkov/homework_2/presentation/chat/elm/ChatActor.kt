package com.krivochkov.homework_2.presentation.chat.elm

import com.krivochkov.homework_2.domain.use_cases.message.GetMessagesUseCase
import com.krivochkov.homework_2.domain.use_cases.message.GetSingleMessageUseCase
import com.krivochkov.homework_2.domain.use_cases.message.SendMessageUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.AddReactionUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.RemoveReactionUseCase
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

class ChatActor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val getSingleMessageUseCase: GetSingleMessageUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase
) : ActorCompat<ChatCommand, ChatEvent> {

    override fun execute(command: ChatCommand): Observable<ChatEvent> = when (command) {
        is ChatCommand.LoadCachedMessages ->
            getMessagesUseCase(command.channelName, command.topicName, cached = true)
                .mapEvents(
                    { messages -> ChatEvent.Internal.CachedMessagesLoaded(messages) },
                    { error -> ChatEvent.Internal.ErrorLoadingCachedMessages(error) }
                )
        is ChatCommand.LoadSingleMessage -> getSingleMessageUseCase(command.messageId)
            .mapEvents(
                { message -> ChatEvent.Internal.SingleMessageLoaded(message) },
                { error -> ChatEvent.Internal.ErrorLoadingSingleMessage(error) }
            )
        is ChatCommand.LoadPage ->
            getMessagesUseCase(
                command.channelName, command.topicName, command.lastMessageId, command.pageSize
            )
                .mapEvents(
                    { messages ->
                        ChatEvent.Internal.PageLoaded(messages, command.lastMessageId == 0L)
                    },
                    { error -> ChatEvent.Internal.ErrorLoadingPage(error) }
                )
        is ChatCommand.SendMessage ->
            sendMessageUseCase(
                command.channelName, command.topicName, command.message, command.attachedFiles
            )
                .mapEvents(
                    successEvent = ChatEvent.Internal.MessageSent,
                    failureEventMapper = { error -> ChatEvent.Internal.ErrorSendingMessage(error) }
                )
        is ChatCommand.AddReaction -> addReactionUseCase(command.messageId, command.emojiName)
            .mapEvents(
                successEvent = ChatEvent.Internal.ReactionUpdated(command.messageId),
                failureEventMapper = { error -> ChatEvent.Internal.ErrorAddingReaction(error) }
            )
        is ChatCommand.RemoveReaction -> removeReactionUseCase(command.messageId, command.emojiName)
            .mapEvents(
                successEvent = ChatEvent.Internal.ReactionUpdated(command.messageId),
                failureEventMapper = { error -> ChatEvent.Internal.ErrorRemovingReaction(error) }
            )
    }
}
package com.krivochkov.homework_2.presentation.message.elm

import com.krivochkov.homework_2.domain.use_cases.message.GetMessagesUseCase
import com.krivochkov.homework_2.domain.use_cases.message.GetSingleMessageUseCase
import com.krivochkov.homework_2.domain.use_cases.message.SendMessageUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.AddReactionUseCase
import com.krivochkov.homework_2.domain.use_cases.reaction.RemoveReactionUseCase
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

class MessageActor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val getSingleMessageUseCase: GetSingleMessageUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val addReactionUseCase: AddReactionUseCase,
    private val removeReactionUseCase: RemoveReactionUseCase
) : ActorCompat<MessageCommand, MessageEvent> {

    override fun execute(command: MessageCommand): Observable<MessageEvent> = when (command) {
        is MessageCommand.LoadCachedMessages ->
            getMessagesUseCase(command.channelName, command.topicName, cached = true)
                .mapEvents(
                    { messages -> MessageEvent.Internal.CachedMessagesLoaded(messages) },
                    { error -> MessageEvent.Internal.ErrorLoadingCachedMessages(error) }
                )
        is MessageCommand.LoadSingleMessage -> getSingleMessageUseCase(command.messageId)
            .mapEvents(
                { message -> MessageEvent.Internal.SingleMessageLoaded(message) },
                { error -> MessageEvent.Internal.ErrorLoadingSingleMessage(error) }
            )
        is MessageCommand.LoadPage ->
            getMessagesUseCase(command.channelName, command.topicName, command.lastMessageId, command.pageSize)
                .mapEvents(
                    { messages -> MessageEvent.Internal.PageLoaded(messages, command.lastMessageId == 0L) },
                    { error -> MessageEvent.Internal.ErrorLoadingPage(error) }
                )
        is MessageCommand.SendMessage ->
            sendMessageUseCase(command.channelName, command.topicName, command.message, command.attachedFiles)
                .mapEvents(
                    successEvent = MessageEvent.Internal.MessageSent(command.channelName, command.topicName),
                    failureEventMapper = { error -> MessageEvent.Internal.ErrorSendingMessage(error) }
                )
        is MessageCommand.AddReaction -> addReactionUseCase(command.messageId, command.emojiName)
            .mapEvents(
                successEvent = MessageEvent.Internal.ReactionUpdated(command.messageId),
                failureEventMapper = { error -> MessageEvent.Internal.ErrorAddingReaction(error) }
            )
        is MessageCommand.RemoveReaction -> removeReactionUseCase(command.messageId, command.emojiName)
            .mapEvents(
                successEvent = MessageEvent.Internal.ReactionUpdated(command.messageId),
                failureEventMapper = { error -> MessageEvent.Internal.ErrorRemovingReaction(error) }
            )
    }
}
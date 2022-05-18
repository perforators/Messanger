package com.krivochkov.homework_2.presentation.channel.create_channel.elm

import com.krivochkov.homework_2.domain.use_cases.channel.CheckChannelForExistenceUseCase
import com.krivochkov.homework_2.domain.use_cases.channel.SubscribeToChannelUseCase
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

class CreateChannelActor(
    private val checkChannelForExistenceUseCase: CheckChannelForExistenceUseCase,
    private val subscribeToChannelUseCase: SubscribeToChannelUseCase
) : ActorCompat<CreateChannelCommand, CreateChannelEvent> {

    override fun execute(command: CreateChannelCommand): Observable<CreateChannelEvent> {
        return when (command) {
            is CreateChannelCommand.CreateChannel ->
                subscribeToChannelUseCase(command.name, command.description)
                    .mapEvents(CreateChannelEvent.Internal.ChannelSuccessCreated) {
                            error -> CreateChannelEvent.Internal.ErrorCreatingChannel(error)
                    }
            is CreateChannelCommand.SubscribeToChannel -> subscribeToChannelUseCase(command.name)
                .mapEvents(CreateChannelEvent.Internal.SuccessfulSubscriptionToChannel) {
                        error -> CreateChannelEvent.Internal.ErrorSubscriptingToChannel(error)
                }
            is CreateChannelCommand.CheckChannelForExistence ->
                checkChannelForExistenceUseCase(command.name)
                    .mapEvents(
                        { channelExists ->
                            if (channelExists) {
                                CreateChannelEvent.Internal.ChannelExists(command.name)
                            } else {
                                CreateChannelEvent.Internal.ChannelNotExists(command.name, command.description)
                            }
                        },
                        { error -> CreateChannelEvent.Internal.ErrorCreatingChannel(error) }
                    )
        }
    }
}
package com.krivochkov.homework_2.presentation.channel.create_channel.elm

import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

class CreateChannelReducer
    : ScreenDslReducer<CreateChannelEvent, CreateChannelEvent.Ui, CreateChannelEvent.Internal, CreateChannelState, CreateChannelEffect, CreateChannelCommand>(
    CreateChannelEvent.Ui::class,
    CreateChannelEvent.Internal::class
) {

    override fun Result.internal(event: CreateChannelEvent.Internal): Any {
        return when (event) {
            is CreateChannelEvent.Internal.ChannelExists -> {
                state { copy(isLoading = false) }
                effects { +CreateChannelEffect.ShowDialogThatChanelExists(event.name) }
            }
            is CreateChannelEvent.Internal.ChannelNotExists -> {
                commands { +CreateChannelCommand.CreateChannel(event.name, event.description) }
            }
            is CreateChannelEvent.Internal.ErrorCreatingChannel -> {
                state { copy(isLoading = false) }
                effects { +CreateChannelEffect.ShowErrorCreatingChannel }
            }
            is CreateChannelEvent.Internal.ErrorSubscriptingToChannel -> {
                state { copy(isLoading = false) }
                effects { +CreateChannelEffect.ShowErrorSubscriptionToChannel }
            }
            is CreateChannelEvent.Internal.ChannelSuccessCreated -> {
                state { copy(isLoading = false) }
                effects {
                    +CreateChannelEffect.ShowToastSuccessfulCreationChannel
                    +CreateChannelEffect.ShowChannelsScreen
                }
            }
            is CreateChannelEvent.Internal.SuccessfulSubscriptionToChannel -> {
                state { copy(isLoading = false) }
                effects {
                    +CreateChannelEffect.ShowToastSuccessfulSubscriptionToChannel
                    +CreateChannelEffect.ShowChannelsScreen
                }
            }
        }
    }

    override fun Result.ui(event: CreateChannelEvent.Ui): Any {
        return when (event) {
            is CreateChannelEvent.Ui.Init -> {}
            is CreateChannelEvent.Ui.CreateChannelButtonClick -> {
                if (event.name.trim().isEmpty()) {
                    effects { +CreateChannelEffect.ShowErrorValidationChannelName }
                } else {
                    state { copy(isLoading = true) }
                    commands {
                        +CreateChannelCommand.CheckChannelForExistence(event.name, event.description)
                    }
                }
            }
            is CreateChannelEvent.Ui.SubscribeToChannel -> {
                state { copy(isLoading = true) }
                commands { +CreateChannelCommand.SubscribeToChannel(event.name) }
            }
            is CreateChannelEvent.Ui.BackButtonClick -> {
                effects { +CreateChannelEffect.NavigateUp }
            }
        }
    }
}
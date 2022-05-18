package com.krivochkov.homework_2.presentation.channel.create_channel.elm

data class CreateChannelState(
    val isLoading: Boolean = false
)

sealed class CreateChannelEvent {

    sealed class Ui : CreateChannelEvent() {
        object Init : Ui()
        object BackButtonClick : Ui()
        data class CreateChannelButtonClick(val name: String, val description: String) : Ui()
        data class SubscribeToChannel(val name: String) : Ui()
    }

    sealed class Internal : CreateChannelEvent() {
        data class ChannelExists(val name: String) : Internal()
        data class ChannelNotExists(val name: String, val description: String) : Internal()
        object ChannelSuccessCreated : Internal()
        object SuccessfulSubscriptionToChannel : Internal()
        data class ErrorCreatingChannel(val throwable: Throwable) : Internal()
        data class ErrorSubscriptingToChannel(val throwable: Throwable) : Internal()
    }
}

sealed class CreateChannelEffect {
    data class ShowDialogThatChanelExists(val channelName: String) : CreateChannelEffect()
    object ShowErrorCreatingChannel : CreateChannelEffect()
    object ShowErrorSubscriptionToChannel : CreateChannelEffect()
    object ShowToastSuccessfulCreationChannel : CreateChannelEffect()
    object ShowToastSuccessfulSubscriptionToChannel : CreateChannelEffect()
    object ShowErrorValidationChannelName : CreateChannelEffect()
    object NavigateUp : CreateChannelEffect()
    object ShowChannelsScreen : CreateChannelEffect()
}

sealed class CreateChannelCommand {
    data class CreateChannel(val name: String, val description: String) : CreateChannelCommand()
    data class SubscribeToChannel(val name: String): CreateChannelCommand()
    data class CheckChannelForExistence(
        val name: String,
        val description: String
    ) : CreateChannelCommand()
}

package com.krivochkov.homework_2.presentation.profile.elm

import com.krivochkov.homework_2.domain.models.User

data class ProfileState(
    val profile: User? = null,
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val isInitialized: Boolean = false,
)

sealed class ProfileEvent {

    sealed class Ui : ProfileEvent() {
        object Init : Ui()
        object LoadMyProfile : Ui()
    }

    sealed class Internal : ProfileEvent() {
        data class ProfileLoaded(val profile: User) : Internal()
        data class ErrorLoadingProfile(val error: Throwable) : Internal()
    }
}

sealed class ProfileEffect

sealed class ProfileCommand {
    object LoadMyProfile : ProfileCommand()
}
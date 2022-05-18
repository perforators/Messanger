package com.krivochkov.homework_2.presentation.profile.my_profile.elm

import com.krivochkov.homework_2.domain.models.User

data class MyProfileState(
    val profile: User? = null,
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val isInitialized: Boolean = false,
)

sealed class MyProfileEvent {

    sealed class Ui : MyProfileEvent() {
        object Init : Ui()
        object LoadMyProfile : Ui()
    }

    sealed class Internal : MyProfileEvent() {
        data class ProfileLoaded(val profile: User) : Internal()
        data class CachedProfileLoaded(val profile: User) : Internal()
        data class ErrorLoadingProfile(val error: Throwable) : Internal()
        data class ErrorLoadingCachedProfile(val error: Throwable) : Internal()
    }
}

sealed class MyProfileEffect {
    object ShowErrorLoadingCachedMyProfile : MyProfileEffect()
    object ShowErrorLoadingActualMyProfile : MyProfileEffect()
}

sealed class MyProfileCommand {
    object LoadMyProfile : MyProfileCommand()
    object LoadCachedMyProfile : MyProfileCommand()
}
package com.krivochkov.homework_2.presentation.profile.my_profile.elm

import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

class ProfileReducer : ScreenDslReducer<MyProfileEvent, MyProfileEvent.Ui, MyProfileEvent.Internal, MyProfileState, MyProfileEffect, MyProfileCommand>(
    MyProfileEvent.Ui::class,
    MyProfileEvent.Internal::class
) {

    override fun Result.internal(event: MyProfileEvent.Internal): Any {
        return when (event) {
            is MyProfileEvent.Internal.ProfileLoaded -> {
                state { copy(isLoading = false, error = null, profile = event.profile) }
            }
            is MyProfileEvent.Internal.CachedProfileLoaded -> {
                state { copy(isLoading = false, error = null, profile = event.profile) }
                commands { +MyProfileCommand.LoadMyProfile }
            }
            is MyProfileEvent.Internal.ErrorLoadingProfile -> {
                if (state.profile == null) {
                    state { copy(isLoading = false, error = event.error) }
                } else {
                    effects { +MyProfileEffect.ShowErrorLoadingActualMyProfile }
                }
            }
            is MyProfileEvent.Internal.ErrorLoadingCachedProfile -> {
                effects { +MyProfileEffect.ShowErrorLoadingCachedMyProfile }
                commands { +MyProfileCommand.LoadMyProfile }
            }
        }
    }

    override fun Result.ui(event: MyProfileEvent.Ui): Any {
        return when (event) {
            is MyProfileEvent.Ui.Init -> {
                if (state.isInitialized.not()) {
                    state { copy(isLoading = true, isInitialized = true, error = null) }
                    commands { +MyProfileCommand.LoadCachedMyProfile }
                } else {
                    Any()
                }
            }
            is MyProfileEvent.Ui.LoadMyProfile -> {
                state { copy(isLoading = true, error = null) }
                commands { +MyProfileCommand.LoadMyProfile }
            }
        }
    }
}
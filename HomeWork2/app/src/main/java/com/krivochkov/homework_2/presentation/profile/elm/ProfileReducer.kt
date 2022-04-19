package com.krivochkov.homework_2.presentation.profile.elm

import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

class ProfileReducer : ScreenDslReducer<ProfileEvent, ProfileEvent.Ui, ProfileEvent.Internal, ProfileState, ProfileEffect, ProfileCommand>(
    ProfileEvent.Ui::class,
    ProfileEvent.Internal::class
) {

    override fun Result.internal(event: ProfileEvent.Internal): Any {
        return when (event) {
            is ProfileEvent.Internal.ProfileLoaded -> {
                state { copy(isLoading = false, error = null, profile = event.profile) }
            }
            is ProfileEvent.Internal.ErrorLoadingProfile -> {
                state { copy(isLoading = false, error = event.error) }
            }
        }
    }

    override fun Result.ui(event: ProfileEvent.Ui): Any {
        return when (event) {
            is ProfileEvent.Ui.Init -> {
                if (state.isInitialized.not()) {
                    state { ProfileState(isLoading = true, isInitialized = true) }
                    commands { +ProfileCommand.LoadMyProfile }
                } else {
                    Any()
                }
            }
            is ProfileEvent.Ui.LoadMyProfile -> {
                state { copy(isLoading = true, error = null) }
                commands { +ProfileCommand.LoadMyProfile }
            }
        }
    }
}
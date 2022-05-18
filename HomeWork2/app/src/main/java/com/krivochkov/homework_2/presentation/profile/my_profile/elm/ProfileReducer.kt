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
            is MyProfileEvent.Internal.ErrorLoadingProfile -> {
                state { copy(isLoading = false, error = event.error) }
            }
        }
    }

    override fun Result.ui(event: MyProfileEvent.Ui): Any {
        return when (event) {
            is MyProfileEvent.Ui.Init -> {
                if (state.isInitialized.not()) {
                    state { copy(isLoading = true, isInitialized = true, error = null) }
                    commands { +MyProfileCommand.LoadMyProfile }
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
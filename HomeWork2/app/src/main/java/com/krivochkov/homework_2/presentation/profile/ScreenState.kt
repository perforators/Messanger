package com.krivochkov.homework_2.presentation.profile

import com.krivochkov.homework_2.domain.models.User

sealed class ScreenState {
    data class ProfileLoaded(val user: User) : ScreenState()
    object Loading : ScreenState()
    object Error : ScreenState()
}
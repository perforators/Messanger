package com.krivochkov.homework_2.presentation.people

import com.krivochkov.homework_2.domain.models.User

sealed class ScreenState {
    data class PeopleLoaded(val users: List<User>) : ScreenState()
    object Loading : ScreenState()
    object Error : ScreenState()
}
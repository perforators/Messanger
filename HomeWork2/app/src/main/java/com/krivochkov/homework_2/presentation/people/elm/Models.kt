package com.krivochkov.homework_2.presentation.people.elm

import com.krivochkov.homework_2.domain.models.User

data class PeopleState(
    val people: List<User> = emptyList(),
    val error: Throwable? = null,
    val isLoading: Boolean = false,
    val isInitialized: Boolean = false,
    val lastQuery: String = ""
)

sealed class PeopleEvent {

    sealed class Ui : PeopleEvent() {
        object Init : Ui()
        data class SearchPeople(val query: String) : Ui()
        data class OnUserClick(val user: User) : Ui()
        object SearchPeopleByLastQuery : Ui()
    }

    sealed class Internal : PeopleEvent() {
        data class PeopleFound(val people: List<User>) : Internal()
        data class ErrorSearchPeople(val error: Throwable) : Internal()
    }
}

sealed class PeopleEffect {
    data class ShowUserDetailScreen(val user: User) : PeopleEffect()
}

sealed class PeopleCommand {
    data class SearchPeople(val query: String = "") : PeopleCommand()
}
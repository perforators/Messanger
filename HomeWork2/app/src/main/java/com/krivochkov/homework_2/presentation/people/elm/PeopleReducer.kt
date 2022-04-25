package com.krivochkov.homework_2.presentation.people.elm

import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer

class PeopleReducer : ScreenDslReducer<PeopleEvent, PeopleEvent.Ui, PeopleEvent.Internal, PeopleState, PeopleEffect, PeopleCommand>(
    PeopleEvent.Ui::class,
    PeopleEvent.Internal::class
) {

    override fun Result.internal(event: PeopleEvent.Internal): Any {
        return when (event) {
            is PeopleEvent.Internal.PeopleFound -> {
                state { copy(isLoading = false, error = null, people = event.people) }
            }
            is PeopleEvent.Internal.ErrorSearchPeople -> {
                state { copy(isLoading = false, error = event.error) }
            }
        }
    }

    override fun Result.ui(event: PeopleEvent.Ui): Any {
        return when (event) {
            is PeopleEvent.Ui.Init -> {
                if (state.isInitialized.not()) {
                    state { copy(isLoading = true, isInitialized = true, error = null) }
                    commands { +PeopleCommand.SearchPeople() }
                } else {
                    Any()
                }
            }
            is PeopleEvent.Ui.SearchPeople -> {
                state { copy(isLoading = true, error = null, lastQuery = event.query) }
                commands { +PeopleCommand.SearchPeople(event.query) }
            }
            is PeopleEvent.Ui.SearchPeopleByLastQuery -> {
                state { copy(isLoading = true, error = null) }
                commands { +PeopleCommand.SearchPeople(state.lastQuery) }
            }
        }
    }
}
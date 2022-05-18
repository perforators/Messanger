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
            is PeopleEvent.Internal.CachedPeopleLoaded -> {
                state {
                    copy(isLoading = event.people.isEmpty(), error = null, people = event.people)
                }
                commands { +PeopleCommand.SearchPeople() }
            }
            is PeopleEvent.Internal.ErrorSearchPeople -> {
                if (state.people.isEmpty()) {
                    state { copy(isLoading = false, error = event.error) }
                } else {
                    state { copy(isLoading = false) }
                    effects { +PeopleEffect.ShowErrorSearchingActualPeople }
                }
            }
            is PeopleEvent.Internal.ErrorLoadingCachedPeople -> {
                effects { +PeopleEffect.ShowErrorLoadingCachedPeople }
                commands { +PeopleCommand.SearchPeople() }
            }
        }
    }

    override fun Result.ui(event: PeopleEvent.Ui): Any {
        return when (event) {
            is PeopleEvent.Ui.Init -> {
                if (state.isInitialized.not()) {
                    state { copy(isLoading = true, isInitialized = true, error = null) }
                    commands { +PeopleCommand.LoadCachedPeople }
                } else {
                    Any()
                }
            }
            is PeopleEvent.Ui.OnUserClick -> {
                effects { +PeopleEffect.ShowUserDetailScreen(event.user) }
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
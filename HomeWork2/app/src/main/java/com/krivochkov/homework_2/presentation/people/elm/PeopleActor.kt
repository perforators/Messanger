package com.krivochkov.homework_2.presentation.people.elm

import com.krivochkov.homework_2.domain.use_cases.user.SearchUsersUseCase
import com.krivochkov.homework_2.presentation.elm_core.Switcher
import io.reactivex.Observable
import vivid.money.elmslie.core.ActorCompat

class PeopleActor(
    private val searchUsersUseCase: SearchUsersUseCase
) : ActorCompat<PeopleCommand, PeopleEvent> {

    private val switcher = Switcher()

    override fun execute(command: PeopleCommand): Observable<PeopleEvent> = when (command) {
        is PeopleCommand.SearchPeople -> switcher.observable {
            searchUsersUseCase(command.query)
                .mapEvents(
                    { list -> PeopleEvent.Internal.PeopleFound(list) },
                    { error -> PeopleEvent.Internal.ErrorSearchPeople(error) }
                )
        }
    }
}
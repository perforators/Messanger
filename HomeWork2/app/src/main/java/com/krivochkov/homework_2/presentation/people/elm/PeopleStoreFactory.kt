package com.krivochkov.homework_2.presentation.people.elm

import com.krivochkov.homework_2.di.people.annotations.PeopleScreenScope
import vivid.money.elmslie.core.ElmStoreCompat
import javax.inject.Inject

@PeopleScreenScope
class PeopleStoreFactory @Inject constructor(
    private val actor: PeopleActor
) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = PeopleState(),
            reducer = PeopleReducer(),
            actor = actor
        )
    }

    fun provide() = store
}
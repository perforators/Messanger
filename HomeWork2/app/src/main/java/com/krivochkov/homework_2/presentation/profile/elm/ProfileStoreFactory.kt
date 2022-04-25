package com.krivochkov.homework_2.presentation.profile.elm

import vivid.money.elmslie.core.ElmStoreCompat

class ProfileStoreFactory(
    private val actor: ProfileActor
) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = ProfileState(),
            reducer = ProfileReducer(),
            actor = actor
        )
    }

    fun provide() = store
}
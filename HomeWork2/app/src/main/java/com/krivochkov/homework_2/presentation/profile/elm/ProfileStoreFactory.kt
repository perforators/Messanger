package com.krivochkov.homework_2.presentation.profile.elm

import com.krivochkov.homework_2.di.profile.annotations.ProfileScreenScope
import vivid.money.elmslie.core.ElmStoreCompat
import javax.inject.Inject

@ProfileScreenScope
class ProfileStoreFactory @Inject constructor(
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
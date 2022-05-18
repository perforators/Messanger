package com.krivochkov.homework_2.presentation.profile.my_profile.elm

import com.krivochkov.homework_2.di.profile.annotations.MyProfileScreenScope
import vivid.money.elmslie.core.ElmStoreCompat
import javax.inject.Inject

@MyProfileScreenScope
class ProfileStoreFactory @Inject constructor(
    private val actor: ProfileActor
) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = MyProfileState(),
            reducer = ProfileReducer(),
            actor = actor
        )
    }

    fun provide() = store
}
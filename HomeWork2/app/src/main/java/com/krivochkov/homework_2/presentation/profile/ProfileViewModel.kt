package com.krivochkov.homework_2.presentation.profile

import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.presentation.profile.elm.ProfileStoreFactory

class ProfileViewModel(profileStoreFactory: ProfileStoreFactory) : ViewModel() {

    val peopleStore = profileStoreFactory.provide()
}
package com.krivochkov.homework_2.presentation.profile.my_profile

import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.presentation.profile.my_profile.elm.ProfileStoreFactory

class MyProfileViewModel(profileStoreFactory: ProfileStoreFactory) : ViewModel() {

    val peopleStore = profileStoreFactory.provide()
}
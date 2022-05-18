package com.krivochkov.homework_2.presentation.profile.my_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.krivochkov.homework_2.presentation.profile.my_profile.elm.ProfileStoreFactory
import javax.inject.Inject

class MyProfileViewModelFactory @Inject constructor(
    private val profileStoreFactory: ProfileStoreFactory
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MyProfileViewModel(profileStoreFactory) as T
    }
}
package com.krivochkov.homework_2.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.krivochkov.homework_2.presentation.profile.elm.ProfileStoreFactory
import javax.inject.Inject

class ProfileViewModelFactory @Inject constructor(
    private val profileStoreFactory: ProfileStoreFactory
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProfileViewModel(profileStoreFactory) as T
    }
}
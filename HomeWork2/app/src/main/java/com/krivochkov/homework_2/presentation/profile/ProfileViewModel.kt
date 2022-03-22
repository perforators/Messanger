package com.krivochkov.homework_2.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.domain.models.User
import com.krivochkov.homework_2.domain.use_cases.user.LoadMyUserProfileUseCase

class ProfileViewModel(
    private val loadMyUserProfileUseCase: LoadMyUserProfileUseCase = LoadMyUserProfileUseCase()
) : ViewModel() {

    private val _myProfile: MutableLiveData<User> = MutableLiveData()
    val myProfile: LiveData<User>
        get() = _myProfile

    init {
        loadMyProfile()
    }

    fun loadMyProfile() {
        _myProfile.value = loadMyUserProfileUseCase()
    }
}
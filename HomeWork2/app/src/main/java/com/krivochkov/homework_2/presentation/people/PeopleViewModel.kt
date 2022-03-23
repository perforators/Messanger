package com.krivochkov.homework_2.presentation.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.domain.models.User
import com.krivochkov.homework_2.domain.use_cases.user.LoadAllUsersUseCase

class PeopleViewModel(
    private val loadAllUsersUseCase: LoadAllUsersUseCase = LoadAllUsersUseCase()
) : ViewModel() {

    private val _users: MutableLiveData<List<User>> = MutableLiveData()
    val users: LiveData<List<User>>
        get() = _users

    init {
        loadUsers()
    }

    fun loadUsers() {
        _users.value = loadAllUsersUseCase()
    }
}
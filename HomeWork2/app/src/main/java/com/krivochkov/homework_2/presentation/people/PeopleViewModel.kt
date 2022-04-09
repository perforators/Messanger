package com.krivochkov.homework_2.presentation.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.domain.models.User
import com.krivochkov.homework_2.domain.use_cases.SearchableUseCase
import com.krivochkov.homework_2.domain.use_cases.user.LoadAllUsersUseCase
import com.krivochkov.homework_2.presentation.search_component.SearchComponent
import com.krivochkov.homework_2.presentation.search_component.SearchComponentImpl
import com.krivochkov.homework_2.presentation.search_component.SearchStatus
import io.reactivex.disposables.CompositeDisposable

class PeopleViewModel(
    private val loadAllUsersUseCase: SearchableUseCase<User> = LoadAllUsersUseCase()
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val searchComponent: SearchComponent<User> by lazy {
        SearchComponentImpl(loadAllUsersUseCase) { user, query ->
            user.fullName.contains(query)
        }
    }

    private val _state: MutableLiveData<ScreenState> = MutableLiveData()
    val state: LiveData<ScreenState>
        get() = _state

    init {
        searchComponent.searchStatus.observeForever { searchStatus ->
            when (searchStatus) {
                is SearchStatus.Success -> _state.value =
                    ScreenState.PeopleLoaded(searchStatus.data)
                is SearchStatus.Error -> _state.value = ScreenState.Error
                is SearchStatus.Searching -> _state.value = ScreenState.Loading
            }
        }

        loadUsers()
    }

    fun loadUsers(query: String = "") {
        searchComponent.search(query)
    }

    fun loadUsersByLastQuery() {
        searchComponent.searchByLastQuery()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
        searchComponent.clearSearch()
    }
}
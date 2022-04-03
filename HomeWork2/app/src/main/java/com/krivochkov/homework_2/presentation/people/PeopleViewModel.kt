package com.krivochkov.homework_2.presentation.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.domain.use_cases.user.LoadAllUsersUseCase
import com.krivochkov.homework_2.utils.hasNotWhitespaces
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class PeopleViewModel(
    private val loadAllUsersUseCase: LoadAllUsersUseCase = LoadAllUsersUseCase()
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private val searchingEvents: PublishSubject<String> = PublishSubject.create()
    private var lastQuery = ""

    private val _state: MutableLiveData<ScreenState> = MutableLiveData()
    val state: LiveData<ScreenState>
        get() = _state

    init {
        initSearchingEventsProcessing()
        loadUsers()
    }

    fun loadUsers(query: String = "") {
        searchingEvents.onNext(query)
    }

    fun loadUsersByLastQuery() {
        loadUsers(lastQuery)
    }

    private fun initSearchingEventsProcessing() {
        searchingEvents
            .filter { it.isEmpty() || it.hasNotWhitespaces() }
            .map { it.trim() }
            .distinctUntilChanged { query, otherQuery ->
                query == otherQuery && state.value !is ScreenState.Error
            }
            .debounce(500, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .switchMapSingle { query ->
                loadAllUsersUseCase { it.fullName.contains(query) }
                    .observeOn(AndroidSchedulers.mainThread())
                    .map {
                        lastQuery = query
                        ScreenState.PeopleLoaded(it) as ScreenState
                    }
                    .onErrorReturn {
                        lastQuery = query
                        ScreenState.Error
                    }
                    .subscribeOn(Schedulers.io())
                    .delaySubscription(3, TimeUnit.SECONDS)
                    .doOnSubscribe { _state.value = ScreenState.Loading }
                    .subscribeOn(AndroidSchedulers.mainThread())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = { _state.value = it } )
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
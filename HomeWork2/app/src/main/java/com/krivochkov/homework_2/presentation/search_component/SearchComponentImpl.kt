package com.krivochkov.homework_2.presentation.search_component

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchComponentImpl<T>(
    private val source: () -> Single<List<T>>,
    private val filter: (data: T, query: String) -> Boolean
) : SearchComponent<T> {

    private val compositeDisposable = CompositeDisposable()

    private val searchingEvents: PublishSubject<SearchQuery> = PublishSubject.create()
    private var lastQuery = SearchQuery()

    private val _searchStatus: MutableLiveData<SearchStatus<T>> = MutableLiveData()
    override val searchStatus: LiveData<SearchStatus<T>>
        get() = _searchStatus

    init {
        initSearchingEventsProcessing()
    }

    override fun search(searchQuery: SearchQuery) {
        searchingEvents.onNext(searchQuery)
    }

    override fun searchByLastQuery() {
        search(lastQuery)
    }

    private fun initSearchingEventsProcessing() {
        searchingEvents
            .map { it.apply { query.trim() } }
            .distinctUntilChanged { query, otherQuery ->
                query.query == otherQuery.query && _searchStatus.value !is SearchStatus.Error
            }
            .debounce(SEARCH_DELAY, TimeUnit.MILLISECONDS)
            .observeOn(Schedulers.io())
            .switchMapSingle { searchQuery ->
                source()
                    .map { it.filter { element -> filter(element, searchQuery.query) } }
                    .map { SearchStatus.Success(it) as SearchStatus<T> }
                    .onErrorReturn { SearchStatus.Error }
                    .subscribeOn(Schedulers.io())
                    .doOnSubscribe {
                        lastQuery = searchQuery
                        if (!searchQuery.ignoreSearchingStatus)
                            _searchStatus.value = SearchStatus.Searching
                    }
                    .subscribeOn(AndroidSchedulers.mainThread())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _searchStatus.value = it },
                onError = { _searchStatus.value = SearchStatus.Error }
            )
            .addTo(compositeDisposable)
    }

    override fun clearSearch() {
        compositeDisposable.dispose()
    }

    companion object {
        private const val SEARCH_DELAY = 500L
    }
}
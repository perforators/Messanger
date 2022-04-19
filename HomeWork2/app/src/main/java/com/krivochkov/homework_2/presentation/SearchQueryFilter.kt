package com.krivochkov.homework_2.presentation

import android.annotation.SuppressLint
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchQueryFilter {

    private val compositeDisposable = CompositeDisposable()

    private val filteredQueries: PublishSubject<String> = PublishSubject.create()
    private val inputQueries: PublishSubject<String> = PublishSubject.create()

    init {
        initInputQueriesProcessing()
    }

    @SuppressLint("CheckResult")
    fun observeFilteredQueries(onNext: (String) -> Unit) {
        filteredQueries.subscribe(onNext)
    }

    fun sendQuery(query: String) {
        inputQueries.onNext(query)
    }

    fun dispose() {
        compositeDisposable.dispose()
    }

    private fun initInputQueriesProcessing() {
        inputQueries
            .map { it.trim() }
            .distinctUntilChanged()
            .debounce(SEARCH_DELAY, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(onNext = { filteredQueries.onNext(it) })
            .addTo(compositeDisposable)
    }

    companion object {
        private const val SEARCH_DELAY = 500L
    }
}
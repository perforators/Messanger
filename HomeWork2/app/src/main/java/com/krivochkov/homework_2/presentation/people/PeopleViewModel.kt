package com.krivochkov.homework_2.presentation.people

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.presentation.SearchQueryFilter
import com.krivochkov.homework_2.presentation.SingleEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

class PeopleViewModel : ViewModel() {

    private val searchQueryFilter = SearchQueryFilter()

    private val _searchQuery: MutableLiveData<SingleEvent<String>> = MutableLiveData()
    val searchQuery: LiveData<SingleEvent<String>>
        get() = _searchQuery

    init {
        searchQueryFilter.getFilterQueriesObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _searchQuery.value = SingleEvent(it) },
                onError = { Log.d(TAG, it.printStackTrace().toString()) }
            )
    }

    fun addQueryToQueue(query: String) {
        searchQueryFilter.sendQuery(query)
    }

    companion object {
        private const val TAG = "PeopleViewModel"
    }
}
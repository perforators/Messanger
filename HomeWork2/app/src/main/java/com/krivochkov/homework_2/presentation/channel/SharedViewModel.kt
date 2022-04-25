package com.krivochkov.homework_2.presentation.channel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.SearchQueryFilter
import com.krivochkov.homework_2.presentation.SingleEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

class SharedViewModel : ViewModel() {

    private val searchQueryFilter = SearchQueryFilter()

    private val _selectedTopic: MutableLiveData<SingleEvent<Pair<Channel, Topic>>> = MutableLiveData()
    val selectedTopic: LiveData<SingleEvent<Pair<Channel, Topic>>>
        get() = _selectedTopic

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

    fun selectTopic(channel: Channel, topic: Topic) {
        _selectedTopic.value = SingleEvent(channel to topic)
    }

    fun sendSearchQuery(query: String) {
        searchQueryFilter.sendQuery(query)
    }

    override fun onCleared() {
        super.onCleared()
        searchQueryFilter.dispose()
    }

    companion object {
        private const val TAG = "ChannelsSharedViewModel"
    }
}
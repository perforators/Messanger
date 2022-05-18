package com.krivochkov.homework_2.presentation.channel.channels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.presentation.SearchQueryFilter
import com.krivochkov.homework_2.presentation.SingleEvent
import com.krivochkov.homework_2.presentation.channel.channels.elm.ChannelStoreFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

class ChannelsViewModel(
    channelsStoreFactory: ChannelStoreFactory,
    private val searchQueryFilter: SearchQueryFilter
) : ViewModel() {

    val channelStore = channelsStoreFactory.provide()

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

    fun sendSearchQuery(query: String) {
        searchQueryFilter.sendQuery(query)
    }

    override fun onCleared() {
        super.onCleared()
        searchQueryFilter.dispose()
    }

    companion object {
        private const val TAG = "ChannelsViewModel"
    }
}
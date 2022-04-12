package com.krivochkov.homework_2.presentation.pagination_component

import androidx.lifecycle.LiveData
import com.krivochkov.homework_2.presentation.SingleEvent

interface PaginationComponent<I> {

    val elements: LiveData<List<I>>

    val event: LiveData<SingleEvent<PaginationEvent>>

    fun requestNextPage(isRetry: Boolean = false)

    fun refreshFirstPage()

    fun refreshElement(selector: (I) -> Boolean)

    fun clear()
}
package com.krivochkov.homework_2.presentation.search_component

sealed class SearchStatus<out T> {
    data class Success<T>(val data: List<T>) : SearchStatus<T>()
    object Searching : SearchStatus<Nothing>()
    object Error : SearchStatus<Nothing>()
}
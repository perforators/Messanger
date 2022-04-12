package com.krivochkov.homework_2.presentation.pagination_component

sealed class PaginationEvent {
    object StartLoadingNextPage : PaginationEvent()
    object SuccessLoadingNextPage: PaginationEvent()
    object FailedLoadingNextPage : PaginationEvent()
    object FailedRefreshElements : PaginationEvent()
}
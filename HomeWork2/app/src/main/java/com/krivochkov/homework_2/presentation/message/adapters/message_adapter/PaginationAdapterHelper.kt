package com.krivochkov.homework_2.presentation.message.adapters.message_adapter

class PaginationAdapterHelper(
    private val onLoadMoreCallback: () -> Unit
) {

    fun onBind(adapterPosition: Int) {
        if (adapterPosition <= DEFAULT_LOAD_MORE_SUBSTITUTIONS) {
            onLoadMoreCallback()
        }
    }

    companion object {
        private const val DEFAULT_LOAD_MORE_SUBSTITUTIONS = 5
    }
}

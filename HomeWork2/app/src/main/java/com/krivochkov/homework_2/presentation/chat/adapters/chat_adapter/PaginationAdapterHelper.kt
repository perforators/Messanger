package com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter

class PaginationAdapterHelper(
    private val onLoadMoreCallback: () -> Unit
) {

    fun onBind(adapterPosition: Int) {
        if (adapterPosition == DEFAULT_LOAD_MORE_SUBSTITUTIONS) {
            onLoadMoreCallback()
        }
    }

    companion object {
        private const val DEFAULT_LOAD_MORE_SUBSTITUTIONS = 3
    }
}

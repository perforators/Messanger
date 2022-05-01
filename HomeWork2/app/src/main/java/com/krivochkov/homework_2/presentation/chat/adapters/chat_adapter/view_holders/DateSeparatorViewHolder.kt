package com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.view_holders

import com.krivochkov.homework_2.databinding.DateSeparatorItemBinding
import com.krivochkov.homework_2.presentation.BaseViewHolder
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.items.DateSeparatorItem

class DateSeparatorViewHolder(private val binding: DateSeparatorItemBinding)
    : BaseViewHolder(binding.root) {

    fun bind(dateSeparatorItem: DateSeparatorItem) {
        binding.dateSeparator.text = dateSeparatorItem.date
    }
}
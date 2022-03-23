package com.krivochkov.homework_2.presentation.message.adapter.view_holders

import com.krivochkov.homework_2.databinding.DateSeparatorItemBinding
import com.krivochkov.homework_2.presentation.BaseViewHolder
import com.krivochkov.homework_2.presentation.message.adapter.items.DateSeparatorItem

class DateSeparatorViewHolder(private val binding: DateSeparatorItemBinding)
    : BaseViewHolder(binding.root) {

    fun bind(dateSeparatorItem: DateSeparatorItem) {
        binding.dateSeparator.text = dateSeparatorItem.date
    }

    companion object {
        const val TYPE_DATE_SEPARATOR = 4
    }
}
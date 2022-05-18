package com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.view_holders

import com.krivochkov.homework_2.databinding.CreateChannelItemBinding
import com.krivochkov.homework_2.presentation.BaseViewHolder

class CreateChannelViewHolder(
    private val binding: CreateChannelItemBinding,
    private val onCreateChannelItemClick: () -> Unit
) : BaseViewHolder(binding.root) {

    fun bind() {
        binding.createChannelItem.setOnClickListener {
            onCreateChannelItemClick()
        }
    }
}
package com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.view_holders

import androidx.appcompat.content.res.AppCompatResources
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.ChannelItemBinding
import com.krivochkov.homework_2.presentation.BaseViewHolder
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.ChannelItem

class ChannelViewHolder(
    private val binding: ChannelItemBinding,
    private val onExpanded: (channelItem: ChannelItem) -> Unit,
    private val onCollapsed: (channelItem: ChannelItem) -> Unit,
) : BaseViewHolder(binding.root) {

    private val context = binding.root.context

    fun bind(channelItem: ChannelItem) {
        binding.channelName.text = channelItem.channel.name
        if (channelItem.isExpanded) showUpArrow() else showDownArrow()

        binding.channelItem.setOnClickListener {
            when (!channelItem.isExpanded) {
                true -> {
                    onExpanded(channelItem)
                }
                false -> {
                    onCollapsed(channelItem)
                }
            }
        }
    }

    private fun showUpArrow() {
        binding.arrow.background =
            AppCompatResources.getDrawable(context, R.drawable.arrow_up_picture)
    }

    private fun showDownArrow() {
        binding.arrow.background =
            AppCompatResources.getDrawable(context, R.drawable.arrow_down_picture)
    }
}
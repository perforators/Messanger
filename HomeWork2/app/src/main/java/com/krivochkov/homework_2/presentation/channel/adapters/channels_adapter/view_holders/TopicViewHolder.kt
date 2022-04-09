package com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.view_holders

import com.krivochkov.homework_2.databinding.TopicItemBinding
import com.krivochkov.homework_2.presentation.BaseViewHolder
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.TopicItem

class TopicViewHolder(
    private val binding: TopicItemBinding,
    private val onTopicClick: (topicItem: TopicItem) -> Unit
) : BaseViewHolder(binding.root) {

    fun bind(topicItem: TopicItem) {
        binding.topicName.text = topicItem.topic.name
        binding.topicItem.setOnClickListener {
            onTopicClick(topicItem)
        }
    }
}
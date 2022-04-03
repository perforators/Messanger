package com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.view_holders

import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.TopicItemBinding
import com.krivochkov.homework_2.presentation.BaseViewHolder
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.TopicItem

class TopicViewHolder(
    private val binding: TopicItemBinding,
    private val onTopicClick: (topicItem: TopicItem) -> Unit
) : BaseViewHolder(binding.root) {

    private val context = binding.root.context

    fun bind(topicItem: TopicItem) {
        binding.topicName.text = topicItem.topic.name
        binding.countMessages.text = String.format(
            context.getString(R.string.count_messages),
            topicItem.topic.countMessages.toString()
        )
        binding.topicItem.setOnClickListener {
            onTopicClick(topicItem)
        }
    }
}
package com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.view_holders

import com.krivochkov.homework_2.databinding.BeginningTopicItemBinding
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.BaseViewHolder
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.items.BeginningTopicItem

class BeginningTopicViewHolder(
    private val binding: BeginningTopicItemBinding,
    private val onClick: (topic: Topic) -> Unit = {}
) : BaseViewHolder(binding.root) {

    fun bind(beginningTopicItem: BeginningTopicItem) {
        binding.topicName.text = beginningTopicItem.topic.name
        binding.beginningTopicItem.setOnClickListener {
            onClick(beginningTopicItem.topic)
        }
    }
}
package com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.view_holders

import com.krivochkov.homework_2.databinding.HeaderTopicItemBinding
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.BaseViewHolder
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.items.HeaderTopicItem

class HeaderTopicViewHolder(
    private val binding: HeaderTopicItemBinding,
    private val onClick: (topic: Topic) -> Unit = {}
) : BaseViewHolder(binding.root) {

    fun bind(headerTopicItem: HeaderTopicItem) {
        binding.topicName.text = headerTopicItem.topic.name
        binding.beginningTopicItem.setOnClickListener {
            onClick(headerTopicItem.topic)
        }
    }
}
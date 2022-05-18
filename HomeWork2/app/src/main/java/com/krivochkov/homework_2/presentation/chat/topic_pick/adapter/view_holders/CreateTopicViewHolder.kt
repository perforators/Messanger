package com.krivochkov.homework_2.presentation.chat.topic_pick.adapter.view_holders

import com.krivochkov.homework_2.databinding.CreateTopicItemBinding
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.BaseViewHolder

class CreateTopicViewHolder(
    private val binding: CreateTopicItemBinding,
    private val onCreateTopicItemClick: (topic: Topic) -> Unit
) : BaseViewHolder(binding.root) {

    fun bind() {
        binding.createTopicButton.setOnClickListener {
            val topicName = binding.topicNameField.editText?.text?.toString().orEmpty()
            onCreateTopicItemClick(Topic(topicName))
        }
    }
}
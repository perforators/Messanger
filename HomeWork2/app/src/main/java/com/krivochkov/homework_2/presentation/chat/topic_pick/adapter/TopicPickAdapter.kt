package com.krivochkov.homework_2.presentation.chat.topic_pick.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.databinding.CreateTopicItemBinding
import com.krivochkov.homework_2.databinding.TopicItemBinding
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.BaseViewHolder
import com.krivochkov.homework_2.presentation.DiffCallback
import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.TopicItem
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.view_holders.TopicViewHolder
import com.krivochkov.homework_2.presentation.chat.topic_pick.adapter.items.CreateTopicItem
import com.krivochkov.homework_2.presentation.chat.topic_pick.adapter.view_holders.CreateTopicViewHolder

class TopicPickAdapter(
    private val onTopicPick: (topic: Topic) -> Unit = {  }
) : RecyclerView.Adapter<BaseViewHolder>() {

    private val differ: AsyncListDiffer<Item> = AsyncListDiffer(this, DiffCallback())

    fun submitItems(items: List<Item>, onCommitted: (() -> Unit)? = null) {
        differ.submitList(items, onCommitted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TopicItem.TYPE -> TopicViewHolder(
                TopicItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onTopicClick = { onTopicPick(it.topic) }
            )
            CreateTopicItem.TYPE -> CreateTopicViewHolder(
                CreateTopicItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onCreateTopicItemClick = { onTopicPick(it) }
            )
            else -> throw IllegalStateException("Unknown viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is TopicViewHolder -> holder.bind(differ.currentList[position] as TopicItem)
            is CreateTopicViewHolder -> holder.bind()
        }
    }

    override fun getItemViewType(position: Int) = differ.currentList[position].getType()

    override fun getItemCount() = differ.currentList.size
}
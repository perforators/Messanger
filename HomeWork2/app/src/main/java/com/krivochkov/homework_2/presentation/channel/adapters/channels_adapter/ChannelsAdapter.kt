package com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.presentation.DiffCallback
import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.ChannelItem
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.TopicItem
import com.krivochkov.homework_2.databinding.ChannelItemBinding
import com.krivochkov.homework_2.databinding.LoadingItemBinding
import com.krivochkov.homework_2.databinding.TopicItemBinding
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.BaseViewHolder
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.LoadingItem
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.view_holders.ChannelViewHolder
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.view_holders.LoadingViewHolder
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.view_holders.TopicViewHolder
import java.lang.IllegalStateException

class ChannelsAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val differ: AsyncListDiffer<Item> = AsyncListDiffer(this, DiffCallback())

    private var onExpandedChannel: (channelId: Long) -> Unit = {  }
    private var onCollapsedChannel: (channelId: Long) -> Unit = {  }
    private var onTopicClick: (channel: Channel, topic: Topic) -> Unit = { _, _ -> }

    fun submitChannels(channels: List<ChannelItem>, onCommitted: (() -> Unit)? = null) {
        val resultItems = mutableListOf<Item>()
        channels.forEach { channelItem ->
            resultItems.add(channelItem)
            if (channelItem.isExpanded) resultItems.addAll(channelItem.childItems)
        }
        submitItems(resultItems, onCommitted)
    }

    private fun submitItems(items: List<Item>, onCommitted: (() -> Unit)? = null) {
        differ.submitList(items, onCommitted)
    }

    private fun findChannelItemById(channelId: Long): ChannelItem? {
        return differ.currentList.find { item ->
            item is ChannelItem && item.channel.id == channelId
        } as? ChannelItem
    }

    fun setOnExpandedChannelListener(listener: (channelId: Long) -> Unit) {
        onExpandedChannel = listener
    }

    fun setOnCollapsedChannelListener(listener: (channelId: Long) -> Unit) {
        onCollapsedChannel = listener
    }

    fun setOnTopicClickListener(listener: (channel: Channel, topic: Topic) -> Unit) {
        onTopicClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            ChannelItem.TYPE -> ChannelViewHolder(
                ChannelItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onExpanded = { onExpandedChannel(it.channel.id) },
                onCollapsed = { onCollapsedChannel(it.channel.id) }
            )
            TopicItem.TYPE -> TopicViewHolder(
                TopicItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onTopicClick = {
                    val channelItem = findChannelItemById(it.associatedChannelId)!!
                    onTopicClick(channelItem.channel, it.topic)
                }
            )
            LoadingItem.TYPE -> LoadingViewHolder(
                LoadingItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalStateException("Unknown viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is ChannelViewHolder -> holder.bind(differ.currentList[position] as ChannelItem)
            is TopicViewHolder -> holder.bind(differ.currentList[position] as TopicItem)
        }
    }

    override fun getItemViewType(position: Int) = differ.currentList[position].getType()

    override fun getItemCount() = differ.currentList.size
}
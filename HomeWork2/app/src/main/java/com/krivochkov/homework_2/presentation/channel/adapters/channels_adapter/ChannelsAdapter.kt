package com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.presentation.DiffCallback
import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.ChannelItem
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.TopicItem
import com.krivochkov.homework_2.databinding.ChannelItemBinding
import com.krivochkov.homework_2.databinding.TopicItemBinding
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import java.lang.IllegalStateException

class ChannelsAdapter : RecyclerView.Adapter<ChannelsAdapter.BaseViewHolder>() {

    private val differ: AsyncListDiffer<Item> = AsyncListDiffer(this, DiffCallback())

    private var onExpandedChannel: (channelId: Long) -> Unit = {  }
    private var onTopicClick: (channel: Channel, topic: Topic) -> Unit = { _, _ -> }

    abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)

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
                channelItem.isExpanded = !channelItem.isExpanded
                when (channelItem.isExpanded) {
                    true -> {
                        onExpanded(channelItem)
                        showUpArrow()
                    }
                    false -> {
                        onCollapsed(channelItem)
                        showDownArrow()
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

    fun submitChannels(channels: List<Channel>) {
        val items = channels.map { ChannelItem(it) }
        submitItems(items)
    }

    fun submitTopicsInChannel(channelId: Long, topics: List<Topic>) {
        val topicItems = topics.map { TopicItem(it, channelId) }
        val channelItem = findChannelItemById(channelId) ?: return
        if (channelItem.isExpanded) {
            collapseChannelItem(channelItem) {
                channelItem.topicItems = topicItems
                expandChannelItem(channelItem)
            }
        } else {
            channelItem.topicItems = topicItems
        }
    }

    private fun expandChannelItem(channelItem: ChannelItem) {
        val items = differ.currentList.toMutableList()
        items.addAll(items.indexOf(channelItem) + 1, channelItem.topicItems)
        submitItems(items)
    }

    private fun collapseChannelItem(channelItem: ChannelItem, onCommitted: (() -> Unit)? = null) {
        val items = differ.currentList.toMutableList()
        items.removeAll(channelItem.topicItems)
        submitItems(items, onCommitted)
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

    fun setOnTopicClickListener(listener: (channel: Channel, topic: Topic) -> Unit) {
        onTopicClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            TYPE_CHANNEL -> ChannelViewHolder(
                ChannelItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onExpanded = { onExpandedChannel(it.channel.id) },
                onCollapsed = { collapseChannelItem(it) },
            )
            TYPE_TOPIC -> TopicViewHolder(
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

    companion object {
        const val TYPE_CHANNEL = 0
        const val TYPE_TOPIC = 1
    }
}
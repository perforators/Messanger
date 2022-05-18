package com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.databinding.*
import com.krivochkov.homework_2.presentation.DiffCallback
import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.domain.models.Emoji
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.BaseViewHolder
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.LoadingItem
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.view_holders.LoadingViewHolder
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.items.HeaderTopicItem
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.items.DateSeparatorItem
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.items.MessageItem
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.view_holders.HeaderTopicViewHolder
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.view_holders.DateSeparatorViewHolder
import com.krivochkov.homework_2.presentation.chat.adapters.chat_adapter.view_holders.MessageViewHolder
import java.lang.IllegalStateException

class ChatAdapter(
    private val paginationAdapterHelper: PaginationAdapterHelper
) : RecyclerView.Adapter<BaseViewHolder>() {

    private val differ: AsyncListDiffer<Item> = AsyncListDiffer(this, DiffCallback())

    var items: List<Item>
        get() = differ.currentList
        set(value) = submitList(value)

    private var onAddMyReaction: (messageId: Long, emoji: Emoji) -> Unit = { _, _ -> }
    private var onRemoveMyReaction: (messageId: Long, emoji: Emoji) -> Unit = { _, _ -> }
    private var onChoosingReaction: (messageId: Long) -> Unit = {  }
    private var onHeaderTopicClick: (topic: Topic) -> Unit = {  }

    private fun submitList(items: List<Item>, onCommitted: (() -> Unit)? = null) {
        differ.submitList(items, onCommitted)
    }

    fun setOnRemoveMyReactionListener(listener: (messageId: Long, emoji: Emoji) -> Unit) {
        onRemoveMyReaction = listener
    }

    fun setOnAddMyReactionListener(listener: (messageId: Long, emoji: Emoji) -> Unit) {
        onAddMyReaction = listener
    }

    fun setOnChoosingReactionListener(listener: (messageId: Long) -> Unit) {
        onChoosingReaction = listener
    }

    fun setOnHeaderTopicClickListener(listener: (topic: Topic) -> Unit) {
        onHeaderTopicClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            MessageItem.TYPE -> MessageViewHolder(
                MessageItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onAddMyReaction,
                onRemoveMyReaction,
                onChoosingReaction
            )
            DateSeparatorItem.TYPE -> DateSeparatorViewHolder(
                DateSeparatorItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            LoadingItem.TYPE -> LoadingViewHolder(
                LoadingItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            HeaderTopicItem.TYPE -> HeaderTopicViewHolder(
                HeaderTopicItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                onHeaderTopicClick
            )
            else -> throw IllegalStateException("Unknown viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is MessageViewHolder -> holder.bind(differ.currentList[position] as MessageItem)
            is DateSeparatorViewHolder -> holder
                .bind(differ.currentList[position] as DateSeparatorItem)
            is HeaderTopicViewHolder -> holder
                .bind(differ.currentList[position] as HeaderTopicItem)
        }
        paginationAdapterHelper.onBind(position)
    }

    override fun getItemViewType(position: Int) = differ.currentList[position].getType()

    override fun getItemCount() = differ.currentList.size
}
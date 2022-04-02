package com.krivochkov.homework_2.presentation.message.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.presentation.DiffCallback
import com.krivochkov.homework_2.presentation.Item
import com.krivochkov.homework_2.databinding.DateSeparatorItemBinding
import com.krivochkov.homework_2.databinding.MessageItemBinding
import com.krivochkov.homework_2.presentation.BaseViewHolder
import com.krivochkov.homework_2.presentation.message.adapter.items.DateSeparatorItem
import com.krivochkov.homework_2.presentation.message.adapter.items.MessageItem
import com.krivochkov.homework_2.presentation.message.adapter.view_holders.DateSeparatorViewHolder
import com.krivochkov.homework_2.presentation.message.adapter.view_holders.MessageViewHolder
import java.lang.IllegalStateException

class MessageAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    private val differ: AsyncListDiffer<Item> = AsyncListDiffer(this, DiffCallback())

    var items: List<Item>
        get() = differ.currentList
        set(value) = submitList(value)

    private var onAddMyReaction: (messageId: Long, emoji: String) -> Unit = { _, _ -> }
    private var onRemoveMyReaction: (messageId: Long, emoji: String) -> Unit = { _, _ -> }
    private var onChoosingReaction: (messageId: Long) -> Unit = {  }

    fun submitList(items: List<Item>, onCommitted: (() -> Unit)? = null) {
        differ.submitList(items, onCommitted)
    }

    fun setOnRemoveMyReactionListener(listener: (messageId: Long, emoji: String) -> Unit) {
        onRemoveMyReaction = listener
    }

    fun setOnAddMyReactionListener(listener: (messageId: Long, emoji: String) -> Unit) {
        onAddMyReaction = listener
    }

    fun setOnChoosingReactionListener(listener: (messageId: Long) -> Unit) {
        onChoosingReaction = listener
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
            else -> throw IllegalStateException("Unknown viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is MessageViewHolder -> holder.bind(differ.currentList[position] as MessageItem)
            is DateSeparatorViewHolder -> holder
                .bind(differ.currentList[position] as DateSeparatorItem)
        }
    }

    override fun getItemViewType(position: Int) = differ.currentList[position].getType()

    override fun getItemCount() = differ.currentList.size
}
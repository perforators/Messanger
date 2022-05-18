package com.krivochkov.homework_2.presentation.people.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.UserItemBinding
import com.krivochkov.homework_2.domain.models.User
import com.krivochkov.homework_2.presentation.profile.utils.ACTIVE_STATUS
import com.krivochkov.homework_2.presentation.profile.utils.IDLE_STATUS
import com.krivochkov.homework_2.presentation.profile.utils.OFFLINE_STATUS
import com.krivochkov.homework_2.utils.loadImage

class PeopleAdapter(
    private val onUserClick: (User) -> Unit = {  }
) : RecyclerView.Adapter<PeopleAdapter.UserViewHolder>() {

    private val differ: AsyncListDiffer<User> = AsyncListDiffer(this, DiffCallback())

    private class DiffCallback : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
    }

    class UserViewHolder(
        private val binding: UserItemBinding,
        private val onUserClick: (User) -> Unit = {  }
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.apply {
                userItem.setOnClickListener {
                    onUserClick(user)
                }
                if (user.avatarUrl == null) {
                    avatar.setImageResource(R.mipmap.ic_launcher)
                } else {
                    avatar.loadImage(user.avatarUrl)
                }
                fullName.text = user.fullName
                email.text = user.email
                when (user.status) {
                    ACTIVE_STATUS -> onlineStatus.setImageResource(R.drawable.is_online_picture)
                    IDLE_STATUS -> onlineStatus.setImageResource(R.drawable.is_idle_picture)
                    OFFLINE_STATUS -> onlineStatus.setImageResource(R.drawable.is_offline_picture)
                    else -> onlineStatus.setImageResource(R.drawable.is_offline_picture)
                }
            }
        }
    }

    fun submitUsers(users: List<User>, onCommitted: (() -> Unit)? = null) {
        differ.submitList(users, onCommitted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onUserClick = onUserClick
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.size
}
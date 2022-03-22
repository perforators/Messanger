package com.krivochkov.homework_2.presentation.people.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.UserItemBinding
import com.krivochkov.homework_2.domain.models.User

class PeopleAdapter : RecyclerView.Adapter<PeopleAdapter.UserViewHolder>() {

    private val differ: AsyncListDiffer<User> = AsyncListDiffer(this, DiffCallback())

    var users: List<User>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private class DiffCallback : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
    }

    class UserViewHolder(private val binding: UserItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.apply {
                avatar.setImageResource(R.mipmap.ic_launcher_round)
                fullName.text = user.fullName
                email.text = user.email
                when (user.isOnline) {
                    true -> onlineStatus.setImageResource(R.drawable.is_online_picture)
                    false -> onlineStatus.setImageResource(R.drawable.is_offline_picture)
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        return UserViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount() = users.size
}
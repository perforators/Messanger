package com.krivochkov.homework_2.presentation.channel.adapters.pager_adapter

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.krivochkov.homework_2.presentation.channel.channels.all_channels.AllChannelsFragment
import com.krivochkov.homework_2.presentation.channel.channels.subscribed_channels.SubscribedChannelsFragment

class PagerStateAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = COUNT_FRAGMENTS

    override fun createFragment(position: Int) = when (position) {
        0 -> SubscribedChannelsFragment.newInstance()
        1 -> AllChannelsFragment.newInstance()
        else -> throw IllegalStateException("Incorrect position")
    }

    companion object {
        private const val COUNT_FRAGMENTS = 2
    }
}
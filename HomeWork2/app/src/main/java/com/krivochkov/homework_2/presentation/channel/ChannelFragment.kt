package com.krivochkov.homework_2.presentation.channel

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.FragmentChannelBinding
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.channel.adapters.pager_adapter.PagerStateAdapter

class ChannelFragment : Fragment(R.layout.fragment_channel) {

    private val binding: FragmentChannelBinding by viewBinding(FragmentChannelBinding::bind)

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabLayout()

        sharedViewModel.selectedTopic.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { result ->
                val (channel, topic) = result
                navigateToTopicChatFragment(channel, topic)
            }
        }

        sharedViewModel.selectedChannel.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { channel ->
                navigateToChannelChatFragment(channel)
            }
        }

        sharedViewModel.showCreateChannelScreenEvent.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                navigateToCreateChannelFragment()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        initSearchLayout()
    }

    private fun initTabLayout() {
        val adapter = PagerStateAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            val tabNames = listOf(
                getString(R.string.tab_subscribed),
                getString(R.string.tab_all),
            )
            tab.text = tabNames[position]
        }.attach()
    }

    private fun initSearchLayout() {
        binding.search.searchView.hint =
            requireContext().getString(R.string.hint_search_view_channels)

        binding.search.searchView.addTextChangedListener { text ->
            sharedViewModel.sendSearchQuery(text?.toString().orEmpty())
        }
    }

    private fun navigateToTopicChatFragment(channel: Channel, topic: Topic) {
        findNavController().navigate(
            ChannelFragmentDirections.actionNavigationChannelsToNavigationTopicChat(channel, topic)
        )
    }

    private fun navigateToChannelChatFragment(channel: Channel) {
        findNavController().navigate(
            ChannelFragmentDirections.actionNavigationChannelsToNavigationChannelChat(channel)
        )
    }

    private fun navigateToCreateChannelFragment() {
        findNavController().navigate(
            ChannelFragmentDirections.actionNavigationChannelsToCreateChannelFragment()
        )
    }
}
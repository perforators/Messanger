package com.krivochkov.homework_2.presentation.channel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.FragmentChannelBinding
import com.krivochkov.homework_2.domain.models.Channel
import com.krivochkov.homework_2.domain.models.Topic
import com.krivochkov.homework_2.presentation.channel.adapters.pager_adapter.PagerStateAdapter

class ChannelFragment : Fragment() {

    private var _binding: FragmentChannelBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChannelBinding.bind(
            inflater.inflate(R.layout.fragment_channel, container, false)
        )
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTabLayout()
        initSearchLayout()

        sharedViewModel.selectedTopic.observe(this) {
            it.getContentIfNotHandled()?.let { result ->
                val (channel, topic) = result
                navigateToMessageFragment(channel, topic)
            }
        }
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
    }

    private fun navigateToMessageFragment(channel: Channel, topic: Topic) {
        findNavController().navigate(
            ChannelFragmentDirections.actionNavigationChannelsToMessagesFragment(channel, topic)
        )
    }
}
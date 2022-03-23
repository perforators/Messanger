package com.krivochkov.homework_2.presentation.channel.channels

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.presentation.channel.SharedViewModel
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.ChannelsAdapter

abstract class BaseChannelsFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: ChannelsAdapter
    protected abstract val channelsViewModel: BaseChannelsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    protected fun initRecycler(recyclerView: RecyclerView) {
        adapter = ChannelsAdapter().apply {
            setOnExpandedChannelListener {
                channelsViewModel.loadTopicsInChannel(it)
            }

            setOnTopicClickListener { channel, topic ->
                sharedViewModel.selectTopic(channel, topic)
            }
        }
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setObservers() {
        channelsViewModel.channels.observe(this) { channels ->
            adapter.submitChannels(channels)
        }

        channelsViewModel.topicsInChannel.observe(this) {
            it.getContentIfNotHandled()?.let { result ->
                val (channelId, topics) = result
                adapter.submitTopicsInChannel(
                    channelId,
                    topics
                )
            }
        }
    }
}
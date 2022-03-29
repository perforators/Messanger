package com.krivochkov.homework_2.presentation.channel.channels

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.presentation.channel.SharedViewModel
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.ChannelsAdapter
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.ChannelItem
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.items.TopicItem
import com.krivochkov.homework_2.presentation.custom_views.ErrorView

abstract class BaseChannelsFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var adapter: ChannelsAdapter
    protected abstract val channelsViewModel: BaseChannelsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObservers()
    }

    override fun onResume() {
        super.onResume()

        sharedViewModel.searchQuery.observe(viewLifecycleOwner) {
            channelsViewModel.loadChannels(it)
        }
    }

    override fun onPause() {
        super.onPause()

        sharedViewModel.searchQuery.removeObservers(viewLifecycleOwner)
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

    protected fun initErrorView(errorView: ErrorView) {
        errorView.setOnErrorButtonClickListener {
            channelsViewModel.loadChannelsByLastQuery()
        }

        errorView.text = requireContext().getString(R.string.error_text)
    }

    private fun setObservers() {
        channelsViewModel.state.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        channelsViewModel.event.observe(viewLifecycleOwner) { singleEvent ->
            singleEvent.getContentIfNotHandled()?.let { event ->
                handleEvent(event)
            }
        }
    }

    private fun render(state: ScreenState) {
        when (state) {
            is ScreenState.ChannelsLoaded -> {
                changeLoadingVisibility(false)
                changeErrorVisibility(false)
                submitChannels(state.channels) {
                    changeContentVisibility(true)
                }
            }
            is ScreenState.Loading -> {
                changeContentVisibility(false)
                changeErrorVisibility(false)
                changeLoadingVisibility(true)
            }
            is ScreenState.Error -> {
                changeContentVisibility(false)
                changeLoadingVisibility(false)
                changeErrorVisibility(true)
            }
        }
    }

    private fun handleEvent(event: UIEvent) {
        when (event) {
            is UIEvent.SubmitTopicsInChannel -> submitTopicsInChannel(event.channelId, event.topics)
            is UIEvent.FailedLoadTopics -> showToast(requireContext()
                .getString(R.string.failed_load_topics))
        }
    }

    protected abstract fun changeLoadingVisibility(visibility: Boolean)

    protected abstract fun changeErrorVisibility(visibility: Boolean)

    protected abstract fun changeContentVisibility(visibility: Boolean)

    private fun submitChannels(channels: List<ChannelItem>, onCommitted: (() -> Unit)? = null) {
        adapter.submitChannels(channels, onCommitted)
    }

    private fun submitTopicsInChannel(channelId: Long, topics: List<TopicItem>) {
        adapter.submitTopicsInChannel(channelId, topics)
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }
}
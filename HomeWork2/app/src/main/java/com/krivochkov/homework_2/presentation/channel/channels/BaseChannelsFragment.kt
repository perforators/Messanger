package com.krivochkov.homework_2.presentation.channel.channels

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.presentation.channel.SharedViewModel
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.ChannelsAdapter
import com.krivochkov.homework_2.presentation.channel.elm.ChannelEffect
import com.krivochkov.homework_2.presentation.channel.elm.ChannelEvent
import com.krivochkov.homework_2.presentation.channel.elm.ChannelState
import com.krivochkov.homework_2.presentation.custom_views.ErrorView
import vivid.money.elmslie.android.base.ElmFragment

abstract class BaseChannelsFragment : ElmFragment<ChannelEvent, ChannelEffect, ChannelState>() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    protected lateinit var adapter: ChannelsAdapter

    override val initEvent: ChannelEvent
        get() = ChannelEvent.Ui.Init

    override fun onResume() {
        super.onResume()

        sharedViewModel.searchQuery.observe(viewLifecycleOwner) { singleEvent ->
            singleEvent.getContentIfNotHandled()?.let { query ->
                store.accept(ChannelEvent.Ui.SearchChannels(query))
            }
        }
    }

    override fun onPause() {
        super.onPause()

        sharedViewModel.searchQuery.removeObservers(viewLifecycleOwner)
    }

    override fun handleEffect(effect: ChannelEffect) {
        when (effect) {
            is ChannelEffect.ShowTopicContent ->
                sharedViewModel.selectTopic(effect.channel, effect.topic)
            is ChannelEffect.ShowErrorLoadingTopics ->
                showToast(requireContext().getString(R.string.failed_load_actual_topics))
            is ChannelEffect.ShowErrorLoadingCachedChannels ->
                showToast(requireContext().getString(R.string.failed_load_cached_channels))
        }
    }

    protected fun initRecycler(recyclerView: RecyclerView) {
        adapter = ChannelsAdapter().apply {
            setOnExpandedChannelListener { channelId ->
                store.accept(ChannelEvent.Ui.ExpandChannel(channelId))
            }

            setOnCollapsedChannelListener { channelId ->
                store.accept(ChannelEvent.Ui.CollapseChannel(channelId))
            }

            setOnTopicClickListener { channel, topic ->
                store.accept(ChannelEvent.Ui.TopicClick(channel, topic))
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
            store.accept(ChannelEvent.Ui.SearchChannelsByLastQuery)
        }

        errorView.text = requireContext().getString(R.string.error_text)
    }

    private fun showToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }
}
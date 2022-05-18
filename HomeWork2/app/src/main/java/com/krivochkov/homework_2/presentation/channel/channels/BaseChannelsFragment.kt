package com.krivochkov.homework_2.presentation.channel.channels

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.presentation.channel.SharedViewModel
import com.krivochkov.homework_2.presentation.channel.adapters.channels_adapter.ChannelsAdapter
import com.krivochkov.homework_2.presentation.channel.channels.elm.ChannelEffect
import com.krivochkov.homework_2.presentation.channel.channels.elm.ChannelEvent
import com.krivochkov.homework_2.presentation.channel.channels.elm.ChannelState
import com.krivochkov.homework_2.presentation.custom_views.ErrorView
import vivid.money.elmslie.android.base.ElmFragment

abstract class BaseChannelsFragment(@LayoutRes contentLayoutId: Int) :
    ElmFragment<ChannelEvent, ChannelEffect, ChannelState>(contentLayoutId) {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    protected val channelsViewModel: ChannelsViewModel by viewModels { channelsViewModelFactory }

    internal abstract var channelsViewModelFactory: ChannelsViewModelFactory

    protected lateinit var adapter: ChannelsAdapter

    override val initEvent: ChannelEvent
        get() = ChannelEvent.Ui.Init

    override fun onResume() {
        super.onResume()

        sharedViewModel.searchQuery.observe(viewLifecycleOwner) { singleEvent ->
            singleEvent.getContentIfNotHandled()?.let { query ->
                channelsViewModel.sendSearchQuery(query)
            }
        }
    }

    override fun onPause() {
        super.onPause()

        sharedViewModel.searchQuery.removeObservers(viewLifecycleOwner)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        channelsViewModel.searchQuery.observe(viewLifecycleOwner) { singleEvent ->
            singleEvent.getContentIfNotHandled()?.let { query ->
                store.accept(ChannelEvent.Ui.SearchChannels(query))
            }
        }
    }

    override fun handleEffect(effect: ChannelEffect) {
        when (effect) {
            is ChannelEffect.ShowTopicContent ->
                sharedViewModel.selectTopic(effect.channel, effect.topic)
            is ChannelEffect.ShowCreateChannelScreen ->
                sharedViewModel.showCreateChannelScreen()
            is ChannelEffect.ShowErrorLoadingCachedTopics ->
                showToast(requireContext().getString(R.string.failed_load_cached_topics))
            is ChannelEffect.ShowErrorLoadingActualTopics ->
                showToast(requireContext().getString(R.string.failed_load_actual_topics))
            is ChannelEffect.ShowErrorLoadingCachedChannels ->
                showToast(requireContext().getString(R.string.failed_load_cached_channels))
            is ChannelEffect.ShowErrorSearchingActualChannels ->
                showToast(requireContext().getString(R.string.failed_search_actual_channels))
            is ChannelEffect.ShowChannelContent -> sharedViewModel.selectChannel(effect.channel)
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

            setOnCreatingChannelItemClickListener {
                store.accept(ChannelEvent.Ui.CreatingChannelItemClick)
            }

            setOnChannelClickListener { channel ->
                store.accept(ChannelEvent.Ui.ChannelClick(channel))
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
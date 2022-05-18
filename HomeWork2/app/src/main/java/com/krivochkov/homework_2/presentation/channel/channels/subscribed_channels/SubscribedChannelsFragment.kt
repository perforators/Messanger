package com.krivochkov.homework_2.presentation.channel.channels.subscribed_channels

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.appComponent
import com.krivochkov.homework_2.databinding.FragmentSubscribedChannelsBinding
import com.krivochkov.homework_2.di.channel.channels.DaggerChannelsScreenComponent
import com.krivochkov.homework_2.di.channel.channels.annotations.SubscribedChannels
import com.krivochkov.homework_2.presentation.channel.channels.BaseChannelsFragment
import com.krivochkov.homework_2.presentation.channel.channels.ChannelsViewModelFactory
import com.krivochkov.homework_2.presentation.channel.channels.elm.ChannelState
import javax.inject.Inject

class SubscribedChannelsFragment : BaseChannelsFragment(R.layout.fragment_subscribed_channels) {

    @Inject
    @SubscribedChannels
    override lateinit var channelsViewModelFactory: ChannelsViewModelFactory

    private val binding: FragmentSubscribedChannelsBinding by viewBinding()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerChannelsScreenComponent.factory()
            .create(appComponent())
            .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler(binding.channels.channelsRecyclerView)
        initErrorView(binding.channels.error)
    }

    override fun render(state: ChannelState) {
        binding.channels.apply {
            channelsRecyclerView.isVisible = state.isLoading.not() && state.error == null

            adapter.submitItems(state.items) {
                loading.loadingLayout.apply {
                    isVisible = state.isLoading
                    if (state.isLoading) startShimmer() else stopShimmer()
                }
            }

            error.isVisible = state.error != null
        }
    }

    override fun createStore() = channelsViewModel.channelStore

    companion object {

        @JvmStatic
        fun newInstance() = SubscribedChannelsFragment()
    }
}
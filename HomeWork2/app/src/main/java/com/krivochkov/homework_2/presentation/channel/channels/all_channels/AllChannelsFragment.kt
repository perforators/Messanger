package com.krivochkov.homework_2.presentation.channel.channels.all_channels

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.appComponent
import com.krivochkov.homework_2.databinding.FragmentAllChannelsBinding
import com.krivochkov.homework_2.di.channel.channels.DaggerChannelsScreenComponent
import com.krivochkov.homework_2.di.channel.channels.annotations.AllChannels
import com.krivochkov.homework_2.presentation.channel.channels.BaseChannelsFragment
import com.krivochkov.homework_2.presentation.channel.channels.ChannelsViewModelFactory
import com.krivochkov.homework_2.presentation.channel.channels.elm.ChannelState
import javax.inject.Inject

class AllChannelsFragment : BaseChannelsFragment(R.layout.fragment_all_channels) {

    @Inject
    @AllChannels
    override lateinit var channelsViewModelFactory: ChannelsViewModelFactory

    private val binding: FragmentAllChannelsBinding by viewBinding()

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
        fun newInstance() = AllChannelsFragment()
    }
}
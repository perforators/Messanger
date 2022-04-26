package com.krivochkov.homework_2.presentation.channel.channels.subscribed_channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.FragmentSubscribedChannelsBinding
import com.krivochkov.homework_2.di.GlobalDI
import com.krivochkov.homework_2.presentation.channel.channels.BaseChannelsFragment
import com.krivochkov.homework_2.presentation.channel.elm.ChannelState

class SubscribedChannelsFragment : BaseChannelsFragment() {

    private var _binding: FragmentSubscribedChannelsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSubscribedChannelsBinding.bind(
            inflater.inflate(R.layout.fragment_subscribed_channels, container, false)
        )
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler(binding.channels.channelsRecyclerView)
        initErrorView(binding.channels.error)
    }

    override fun render(state: ChannelState) {
        binding.channels.apply {
            channelsRecyclerView.isVisible = state.isLoading.not() && state.error == null

            adapter.submitChannels(state.channels) {
                loading.loadingLayout.apply {
                    isVisible = state.isLoading
                    if (state.isLoading) startShimmer() else stopShimmer()
                }
            }

            error.isVisible = state.error != null
        }
    }

    override fun createStore() =
        GlobalDI.INSTANCE.presentationModule.subscribedChannelsStoreFactory.provide()

    companion object {

        @JvmStatic
        fun newInstance() = SubscribedChannelsFragment()
    }
}
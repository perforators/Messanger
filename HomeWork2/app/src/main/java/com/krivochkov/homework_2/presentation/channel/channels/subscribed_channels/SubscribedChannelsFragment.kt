package com.krivochkov.homework_2.presentation.channel.channels.subscribed_channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.FragmentSubscribedChannelsBinding
import com.krivochkov.homework_2.presentation.channel.channels.BaseChannelsFragment

class SubscribedChannelsFragment : BaseChannelsFragment() {

    private var _binding: FragmentSubscribedChannelsBinding? = null
    private val binding get() = _binding!!

    override val channelsViewModel: SubscribedChannelsViewModel by viewModels()

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

    override fun changeLoadingVisibility(visibility: Boolean) {
        binding.channels.loading.loadingLayout.apply {
            isVisible = visibility
            if (visibility) startShimmer() else stopShimmer()
        }
    }

    override fun changeErrorVisibility(visibility: Boolean) {
        binding.channels.error.isVisible = visibility
    }

    override fun changeContentVisibility(visibility: Boolean) {
        binding.channels.channelsRecyclerView.isVisible = visibility
    }

    companion object {

        @JvmStatic
        fun newInstance() = SubscribedChannelsFragment()
    }
}
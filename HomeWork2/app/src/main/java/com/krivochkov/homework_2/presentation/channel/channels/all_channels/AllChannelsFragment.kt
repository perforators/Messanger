package com.krivochkov.homework_2.presentation.channel.channels.all_channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.databinding.FragmentAllChannelsBinding
import com.krivochkov.homework_2.presentation.channel.channels.BaseChannelsFragment

class AllChannelsFragment : BaseChannelsFragment() {

    private var _binding: FragmentAllChannelsBinding? = null
    private val binding get() = _binding!!

    override val channelsViewModel: AllChannelsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllChannelsBinding.bind(
            inflater.inflate(R.layout.fragment_all_channels, container, false)
        )
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler(binding.streamsRecyclerView)
        setObservers()
    }

    companion object {

        @JvmStatic
        fun newInstance() = AllChannelsFragment()
    }
}
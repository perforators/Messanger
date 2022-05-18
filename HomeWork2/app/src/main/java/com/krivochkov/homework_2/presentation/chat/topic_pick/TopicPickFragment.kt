package com.krivochkov.homework_2.presentation.chat.topic_pick

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.appComponent
import com.krivochkov.homework_2.databinding.FragmentTopicPickBinding
import com.krivochkov.homework_2.di.topic_pick.DaggerTopicPickScreenComponent
import com.krivochkov.homework_2.presentation.chat.TopicPickSharedViewModel
import com.krivochkov.homework_2.presentation.chat.topic_pick.adapter.TopicPickAdapter
import com.krivochkov.homework_2.presentation.chat.topic_pick.elm.TopicPickEffect
import com.krivochkov.homework_2.presentation.chat.topic_pick.elm.TopicPickEvent
import com.krivochkov.homework_2.presentation.chat.topic_pick.elm.TopicPickState
import vivid.money.elmslie.android.base.ElmFragment
import javax.inject.Inject

class TopicPickFragment
    : ElmFragment<TopicPickEvent, TopicPickEffect, TopicPickState>(R.layout.fragment_topic_pick) {

    @Inject
    internal lateinit var topicPickViewModelFactory: TopicPickViewModelFactory

    private val viewModel: TopicPickViewModel by viewModels { topicPickViewModelFactory }
    private val topicPickSharedViewModel: TopicPickSharedViewModel by activityViewModels()

    private val args by navArgs<TopicPickFragmentArgs>()

    private val channelId: Long
        get() = args.channelId

    private val binding: FragmentTopicPickBinding by viewBinding()

    private lateinit var adapter: TopicPickAdapter

    override val initEvent: TopicPickEvent
        get() = TopicPickEvent.Ui.Init(channelId)

    override fun createStore() = viewModel.topicPickStore

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerTopicPickScreenComponent.factory()
            .create(appComponent())
            .inject(this)
    }

    override fun render(state: TopicPickState) {
        binding.apply {
            topicPickRecycler.isVisible = state.isLoading.not()
            adapter.submitItems(state.topics) {
                loading.isVisible = state.isLoading
            }
        }
    }

    override fun handleEffect(effect: TopicPickEffect) {
        when (effect) {
            is TopicPickEffect.ShowErrorValidationTopic ->
                showToast(R.string.error_validation_topic_name)
            is TopicPickEffect.ShowErrorLoadingTopics ->
                showToast(R.string.failed_load_actual_topics)
            is TopicPickEffect.NavigateUp -> findNavController().navigateUp()
            is TopicPickEffect.SendTopicToChat -> topicPickSharedViewModel.sendTopic(effect.topic)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        initToolbar()
    }

    private fun initRecycler() {
        adapter = TopicPickAdapter {
            store.accept(TopicPickEvent.Ui.TopicClick(it))
        }
        binding.topicPickRecycler.adapter = adapter
        binding.topicPickRecycler.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun initToolbar() {
        binding.toolbarLayout.apply {
            toolbar.title = getString(R.string.topic_picker)
            backButton.setOnClickListener {
                store.accept(TopicPickEvent.Ui.NavigateUp)
            }
        }
    }

    private fun showToast(stringResId: Int) {
        val text = requireContext().getString(stringResId)
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }
}
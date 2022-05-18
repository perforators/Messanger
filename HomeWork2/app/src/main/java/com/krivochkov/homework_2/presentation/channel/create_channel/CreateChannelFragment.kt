package com.krivochkov.homework_2.presentation.channel.create_channel

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.krivochkov.homework_2.R
import com.krivochkov.homework_2.appComponent
import com.krivochkov.homework_2.databinding.FragmentCreateChannelBinding
import com.krivochkov.homework_2.di.channel.create_channel.DaggerCreateChannelScreenComponent
import com.krivochkov.homework_2.presentation.channel.create_channel.elm.CreateChannelEffect
import com.krivochkov.homework_2.presentation.channel.create_channel.elm.CreateChannelEvent
import com.krivochkov.homework_2.presentation.channel.create_channel.elm.CreateChannelState
import vivid.money.elmslie.android.base.ElmFragment
import javax.inject.Inject

class CreateChannelFragment :
    ElmFragment<CreateChannelEvent, CreateChannelEffect, CreateChannelState>(R.layout.fragment_create_channel) {

    @Inject
    internal lateinit var createChannelViewModelFactory: CreateChannelViewModelFactory

    private val binding: FragmentCreateChannelBinding by viewBinding(
        FragmentCreateChannelBinding::bind
    )

    private val viewModel: CreateChannelViewModel by viewModels { createChannelViewModelFactory }

    override val initEvent: CreateChannelEvent
        get() = CreateChannelEvent.Ui.Init

    override fun onAttach(context: Context) {
        super.onAttach(context)
        DaggerCreateChannelScreenComponent.factory()
            .create(appComponent())
            .inject(this)
    }

    override fun createStore() = viewModel.createChannelStore

    override fun render(state: CreateChannelState) {
        binding.apply {
            createChannelLayout.createChannel.isVisible = state.isLoading.not()
            loading.isVisible = state.isLoading
        }
    }

    override fun handleEffect(effect: CreateChannelEffect) {
        when (effect) {
            is CreateChannelEffect.ShowDialogThatChanelExists ->
                showDialogThatChannelExists(effect.channelName)
            is CreateChannelEffect.ShowErrorCreatingChannel ->
                showToast(R.string.failed_create_channel)
            is CreateChannelEffect.ShowErrorSubscriptionToChannel ->
                showToast(R.string.failed_subscribe_to_channel)
            is CreateChannelEffect.ShowToastSuccessfulCreationChannel ->
                showToast(R.string.successful_creation_channel)
            is CreateChannelEffect.ShowToastSuccessfulSubscriptionToChannel ->
                showToast(R.string.successful_subscription_to_channel)
            is CreateChannelEffect.ShowErrorValidationChannelName ->
                showToast(R.string.error_validation_channel_name)
            is CreateChannelEffect.NavigateUp -> findNavController().navigateUp()
            is CreateChannelEffect.ShowChannelsScreen -> navigateToChannelsScreen()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initInputFields()
    }

    private fun initInputFields() {
        binding.createChannelLayout.apply {
            createChannelButton.setOnClickListener {
                val channelName = channelNameField.editText?.text?.toString().orEmpty()
                val description = channelDescriptionField.editText?.text?.toString().orEmpty()
                store.accept(
                    CreateChannelEvent.Ui.CreateChannelButtonClick(channelName, description)
                )
            }
        }
    }

    private fun initToolbar() {
        binding.toolbarLayout.toolbar.apply {
            title = requireContext().getString(R.string.creating_channel)
        }

        binding.toolbarLayout.backButton.setOnClickListener {
            store.accept(CreateChannelEvent.Ui.BackButtonClick)
        }
    }

    private fun showToast(@StringRes stringResId: Int) {
        val text = requireContext().getString(stringResId)
        Toast.makeText(requireContext(), text, Toast.LENGTH_LONG).show()
    }

    private fun showDialogThatChannelExists(channelName: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_channel_exists_title)
            .setMessage(R.string.dialog_channel_exists_message)
            .setPositiveButton(R.string.dialog_channel_exists_positive_button) { dialog, _ ->
                store.accept(CreateChannelEvent.Ui.SubscribeToChannel(channelName))
                dialog.dismiss()
            }
            .setNegativeButton(R.string.dialog_channel_exists_negative_button) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun navigateToChannelsScreen() {
        findNavController().navigate(
            CreateChannelFragmentDirections.actionNavigationCreateChannelToNavigationChannels()
        )
    }
}
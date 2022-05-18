package com.krivochkov.homework_2.presentation.channel.create_channel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.krivochkov.homework_2.presentation.channel.create_channel.elm.CreateChannelStoreFactory
import javax.inject.Inject

class CreateChannelViewModelFactory @Inject constructor(
    private val createChannelStoreFactory: CreateChannelStoreFactory
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CreateChannelViewModel(createChannelStoreFactory) as T
    }
}
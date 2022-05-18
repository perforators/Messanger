package com.krivochkov.homework_2.presentation.channel.create_channel

import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.presentation.channel.create_channel.elm.CreateChannelStoreFactory

class CreateChannelViewModel(
    createChannelStoreFactory: CreateChannelStoreFactory
) : ViewModel() {

    val createChannelStore = createChannelStoreFactory.provide()
}
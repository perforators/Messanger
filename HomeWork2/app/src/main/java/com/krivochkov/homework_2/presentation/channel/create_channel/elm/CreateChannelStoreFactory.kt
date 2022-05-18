package com.krivochkov.homework_2.presentation.channel.create_channel.elm

import com.krivochkov.homework_2.di.channel.create_channel.annotations.CreateChannelScreenScope
import vivid.money.elmslie.core.ElmStoreCompat
import javax.inject.Inject

@CreateChannelScreenScope
class CreateChannelStoreFactory @Inject constructor(
    private val actor: CreateChannelActor
) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = CreateChannelState(),
            reducer = CreateChannelReducer(),
            actor = actor
        )
    }

    fun provide() = store
}
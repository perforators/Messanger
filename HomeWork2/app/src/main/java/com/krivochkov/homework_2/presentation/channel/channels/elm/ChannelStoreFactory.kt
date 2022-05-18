package com.krivochkov.homework_2.presentation.channel.channels.elm

import vivid.money.elmslie.core.ElmStoreCompat

class ChannelStoreFactory(
    private val actor: ChannelActor
) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = ChannelState(),
            reducer = ChannelReducer(),
            actor = actor
        )
    }

    fun provide() = store
}

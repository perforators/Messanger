package com.krivochkov.homework_2.presentation.message.elm

import vivid.money.elmslie.core.ElmStoreCompat

class MessageStoreFactory(
    private val actor: MessageActor
) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = MessageState(),
            reducer = MessageReducer(),
            actor = actor
        )
    }

    fun provide() = store
}
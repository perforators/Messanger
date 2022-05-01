package com.krivochkov.homework_2.presentation.chat.elm

import com.krivochkov.homework_2.di.chat.annotations.ChatScreenScope
import vivid.money.elmslie.core.ElmStoreCompat
import javax.inject.Inject

@ChatScreenScope
class ChatStoreFactory @Inject constructor(
    private val actor: ChatActor
) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = ChatState(),
            reducer = ChatReducer(),
            actor = actor
        )
    }

    fun provide() = store
}
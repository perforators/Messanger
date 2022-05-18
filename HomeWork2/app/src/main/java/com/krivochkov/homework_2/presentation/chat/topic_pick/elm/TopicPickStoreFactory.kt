package com.krivochkov.homework_2.presentation.chat.topic_pick.elm

import vivid.money.elmslie.core.ElmStoreCompat
import javax.inject.Inject

class TopicPickStoreFactory @Inject constructor(
    private val actor: TopicPickActor
) {

    private val store by lazy {
        ElmStoreCompat(
            initialState = TopicPickState(),
            reducer = TopicPickReducer(),
            actor = actor
        )
    }

    fun provide() = store
}
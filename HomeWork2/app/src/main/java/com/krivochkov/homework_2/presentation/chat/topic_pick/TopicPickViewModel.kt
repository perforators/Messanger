package com.krivochkov.homework_2.presentation.chat.topic_pick

import androidx.lifecycle.ViewModel
import com.krivochkov.homework_2.presentation.chat.topic_pick.elm.TopicPickStoreFactory

class TopicPickViewModel(
    topicPickStoreFactory: TopicPickStoreFactory
) : ViewModel() {

    val topicPickStore = topicPickStoreFactory.provide()
}
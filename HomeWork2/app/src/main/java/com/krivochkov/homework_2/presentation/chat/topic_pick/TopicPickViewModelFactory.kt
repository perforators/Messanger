package com.krivochkov.homework_2.presentation.chat.topic_pick

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.krivochkov.homework_2.presentation.chat.topic_pick.elm.TopicPickStoreFactory
import javax.inject.Inject

class TopicPickViewModelFactory @Inject constructor(
    private val topicPickStoreFactory: TopicPickStoreFactory
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TopicPickViewModel(topicPickStoreFactory) as T
    }
}
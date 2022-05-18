package com.krivochkov.homework_2.di.topic_pick.modules

import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCase
import com.krivochkov.homework_2.presentation.chat.topic_pick.elm.TopicPickActor
import dagger.Module
import dagger.Provides

@Module
class TopicPickElmModule {

    @Provides
    fun provideTopicPickActor(loadTopicsUseCase: LoadTopicsUseCase) = TopicPickActor(loadTopicsUseCase)
}
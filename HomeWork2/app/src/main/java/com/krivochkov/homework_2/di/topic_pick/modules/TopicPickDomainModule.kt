package com.krivochkov.homework_2.di.topic_pick.modules

import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCase
import com.krivochkov.homework_2.domain.use_cases.topic.LoadTopicsUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
interface TopicPickDomainModule {

    @Binds
    fun bindLoadTopicsUseCase(impl: LoadTopicsUseCaseImpl): LoadTopicsUseCase
}
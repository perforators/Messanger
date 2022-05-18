package com.krivochkov.homework_2.di.topic_pick

import com.krivochkov.homework_2.di.topic_pick.annotations.TopicPickScreenScope
import com.krivochkov.homework_2.di.topic_pick.modules.TopicPickDomainModule
import com.krivochkov.homework_2.di.topic_pick.modules.TopicPickElmModule
import com.krivochkov.homework_2.presentation.chat.topic_pick.TopicPickFragment
import dagger.Component

@TopicPickScreenScope
@Component(
    modules = [TopicPickDomainModule::class, TopicPickElmModule::class],
    dependencies = [TopicPickScreenDependencies::class]
)
interface TopicPickScreenComponent {

    fun inject(fragment: TopicPickFragment)

    @Component.Factory
    interface Factory {

        fun create(dependencies: TopicPickScreenDependencies): TopicPickScreenComponent
    }
}

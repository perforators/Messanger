package com.krivochkov.homework_2.di.chat

import com.krivochkov.homework_2.di.application.ApplicationComponent
import com.krivochkov.homework_2.di.chat.annotations.ChatScreenScope
import com.krivochkov.homework_2.di.chat.modules.ChatDomainModule
import com.krivochkov.homework_2.di.chat.modules.ChatElmModule
import com.krivochkov.homework_2.di.chat.modules.ChatViewModelModule
import com.krivochkov.homework_2.presentation.chat.ChatFragment
import dagger.Component

@ChatScreenScope
@Component(
    modules = [
        ChatDomainModule::class,
        ChatElmModule::class,
        ChatViewModelModule::class
    ],
    dependencies = [ApplicationComponent::class]
)
interface ChatScreenComponent {

    fun inject(fragment: ChatFragment)

    @Component.Factory
    interface Factory {

        fun create(applicationComponent: ApplicationComponent): ChatScreenComponent
    }
}
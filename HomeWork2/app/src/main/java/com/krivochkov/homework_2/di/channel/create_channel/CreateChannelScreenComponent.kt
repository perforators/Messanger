package com.krivochkov.homework_2.di.channel.create_channel

import com.krivochkov.homework_2.di.channel.create_channel.annotations.CreateChannelScreenScope
import com.krivochkov.homework_2.di.channel.create_channel.modules.CreateChannelDomainModule
import com.krivochkov.homework_2.di.channel.create_channel.modules.CreateChannelElmModule
import com.krivochkov.homework_2.presentation.channel.create_channel.CreateChannelFragment
import dagger.Component

@CreateChannelScreenScope
@Component(
    modules = [CreateChannelDomainModule::class, CreateChannelElmModule::class],
    dependencies = [CreateChannelScreenDependencies::class]
)
interface CreateChannelScreenComponent {

    fun inject(fragment: CreateChannelFragment)

    @Component.Factory
    interface Factory {

        fun create(dependencies: CreateChannelScreenDependencies): CreateChannelScreenComponent
    }
}
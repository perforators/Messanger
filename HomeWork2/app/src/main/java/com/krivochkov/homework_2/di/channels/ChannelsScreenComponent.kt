package com.krivochkov.homework_2.di.channels

import com.krivochkov.homework_2.di.application.ApplicationComponent
import com.krivochkov.homework_2.di.channels.annotations.ChannelsScreenScope
import com.krivochkov.homework_2.di.channels.modules.ChannelsDomainModule
import com.krivochkov.homework_2.di.channels.modules.ChannelsElmModule
import com.krivochkov.homework_2.di.channels.modules.ChannelsViewModelModule
import com.krivochkov.homework_2.presentation.channel.channels.all_channels.AllChannelsFragment
import com.krivochkov.homework_2.presentation.channel.channels.subscribed_channels.SubscribedChannelsFragment
import dagger.Component

@ChannelsScreenScope
@Component(
    modules = [
        ChannelsDomainModule::class,
        ChannelsElmModule::class,
        ChannelsViewModelModule::class
    ],
    dependencies = [ApplicationComponent::class]
)
interface ChannelsScreenComponent {

    fun inject(fragment: AllChannelsFragment)

    fun inject(fragment: SubscribedChannelsFragment)

    @Component.Factory
    interface Factory {

        fun create(applicationComponent: ApplicationComponent): ChannelsScreenComponent
    }
}
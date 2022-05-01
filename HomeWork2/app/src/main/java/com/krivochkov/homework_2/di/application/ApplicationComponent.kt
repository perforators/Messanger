package com.krivochkov.homework_2.di.application

import android.content.Context
import com.krivochkov.homework_2.di.application.modules.CoreModule
import com.krivochkov.homework_2.di.application.modules.PersistenceModule
import com.krivochkov.homework_2.di.application.modules.NetworkModule
import com.krivochkov.homework_2.di.application.modules.RepositoryModule
import com.krivochkov.homework_2.di.channels.ChannelsScreenDependencies
import com.krivochkov.homework_2.di.chat.ChatScreenDependencies
import com.krivochkov.homework_2.di.people.PeopleScreenDependencies
import com.krivochkov.homework_2.di.profile.ProfileScreenDependencies
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    NetworkModule::class,
    PersistenceModule::class,
    CoreModule::class,
    RepositoryModule::class
])
interface ApplicationComponent :
    ChannelsScreenDependencies, ChatScreenDependencies, PeopleScreenDependencies, ProfileScreenDependencies {

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}
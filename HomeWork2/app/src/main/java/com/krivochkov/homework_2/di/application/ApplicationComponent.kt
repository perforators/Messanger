package com.krivochkov.homework_2.di.application

import android.content.Context
import com.krivochkov.homework_2.di.application.modules.CoreModule
import com.krivochkov.homework_2.di.application.modules.PersistenceModule
import com.krivochkov.homework_2.di.application.modules.NetworkModule
import com.krivochkov.homework_2.di.application.modules.RepositoryModule
import com.krivochkov.homework_2.domain.repositories.AttachedFileRepository
import com.krivochkov.homework_2.domain.repositories.ChannelRepository
import com.krivochkov.homework_2.domain.repositories.MessageRepository
import com.krivochkov.homework_2.domain.repositories.UserRepository
import com.krivochkov.homework_2.presentation.SearchQueryFilter
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
interface ApplicationComponent {

    fun getChannelRepository(): ChannelRepository

    fun getMessageRepository(): MessageRepository

    fun getUserRepository(): UserRepository

    fun getAttachedFileRepository(): AttachedFileRepository

    fun getSearchQueryFilter(): SearchQueryFilter

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}
package com.krivochkov.homework_2.di.people

import com.krivochkov.homework_2.di.application.ApplicationComponent
import com.krivochkov.homework_2.di.people.annotations.PeopleScreenScope
import com.krivochkov.homework_2.di.people.modules.PeopleDomainModule
import com.krivochkov.homework_2.di.people.modules.PeopleElmModule
import com.krivochkov.homework_2.di.people.modules.PeopleViewModelModule
import com.krivochkov.homework_2.presentation.people.PeopleFragment
import dagger.Component

@PeopleScreenScope
@Component(
    modules = [
        PeopleDomainModule::class,
        PeopleElmModule::class,
        PeopleViewModelModule::class
    ],
    dependencies = [ApplicationComponent::class]
)
interface PeopleScreenComponent {

    fun inject(fragment: PeopleFragment)

    @Component.Factory
    interface Factory {

        fun create(applicationComponent: ApplicationComponent): PeopleScreenComponent
    }
}
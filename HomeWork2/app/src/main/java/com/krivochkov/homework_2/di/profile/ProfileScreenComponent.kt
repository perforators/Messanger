package com.krivochkov.homework_2.di.profile

import com.krivochkov.homework_2.di.application.ApplicationComponent
import com.krivochkov.homework_2.di.profile.annotations.ProfileScreenScope
import com.krivochkov.homework_2.di.profile.modules.ProfileDomainModule
import com.krivochkov.homework_2.di.profile.modules.ProfileElmModule
import com.krivochkov.homework_2.di.profile.modules.ProfileViewModelModule
import com.krivochkov.homework_2.presentation.profile.ProfileFragment
import dagger.Component

@ProfileScreenScope
@Component(
    modules = [
        ProfileDomainModule::class,
        ProfileElmModule::class,
        ProfileViewModelModule::class
    ],
    dependencies = [ApplicationComponent::class]
)
interface ProfileScreenComponent {

    fun inject(fragment: ProfileFragment)

    @Component.Factory
    interface Factory {

        fun create(applicationComponent: ApplicationComponent): ProfileScreenComponent
    }
}
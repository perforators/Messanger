package com.krivochkov.homework_2.di.profile

import com.krivochkov.homework_2.di.profile.annotations.MyProfileScreenScope
import com.krivochkov.homework_2.di.profile.modules.MyProfileDomainModule
import com.krivochkov.homework_2.di.profile.modules.MyProfileElmModule
import com.krivochkov.homework_2.presentation.profile.my_profile.MyProfileFragment
import dagger.Component

@MyProfileScreenScope
@Component(
    modules = [MyProfileDomainModule::class, MyProfileElmModule::class],
    dependencies = [MyProfileScreenDependencies::class]
)
interface MyProfileScreenComponent {

    fun inject(fragment: MyProfileFragment)

    @Component.Factory
    interface Factory {

        fun create(dependencies: MyProfileScreenDependencies): MyProfileScreenComponent
    }
}
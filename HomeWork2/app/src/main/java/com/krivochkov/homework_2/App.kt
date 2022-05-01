package com.krivochkov.homework_2

import android.app.Application
import androidx.fragment.app.Fragment
import com.krivochkov.homework_2.di.application.ApplicationComponent
import com.krivochkov.homework_2.di.application.DaggerApplicationComponent

class App : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}

fun Fragment.appComponent() = (requireActivity().application as App).applicationComponent
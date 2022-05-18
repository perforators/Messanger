package com.krivochkov.homework_2

import android.app.Application
import androidx.fragment.app.Fragment
import com.krivochkov.homework_2.di.application.ApplicationComponent
import com.krivochkov.homework_2.di.application.DaggerApplicationComponent
import vivid.money.elmslie.android.logger.strategy.AndroidLog
import vivid.money.elmslie.core.config.ElmslieConfig

class App : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        ElmslieConfig.logger { always(AndroidLog.E) }
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}

fun Fragment.appComponent() = (requireActivity().application as App).applicationComponent
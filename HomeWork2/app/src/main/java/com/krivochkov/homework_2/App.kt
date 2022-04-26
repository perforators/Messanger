package com.krivochkov.homework_2

import android.app.Application
import com.krivochkov.homework_2.di.GlobalDI

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        GlobalDI.init(this)
    }
}
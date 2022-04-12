package com.krivochkov.homework_2

import android.app.Application
import com.krivochkov.homework_2.data.sources.local.database.DatabaseInstance

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // пока так инициализирую, чтобы лишней мороки не создавать
        // как dagger пройдём, сделаю по нормальному
        DatabaseInstance.initDatabase(this)
    }
}
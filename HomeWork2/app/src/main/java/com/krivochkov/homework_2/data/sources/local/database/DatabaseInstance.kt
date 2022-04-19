package com.krivochkov.homework_2.data.sources.local.database

import android.app.Application
import androidx.room.Room

object DatabaseInstance {

    private const val DB_NAME = "App database"
    var instance: AppDatabase? = null

    fun initDatabase(application: Application) {
        instance = Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            DB_NAME
        ).build()
    }
}
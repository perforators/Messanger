package com.krivochkov.homework_2.di

import android.app.Application

class GlobalDI private constructor(private val application: Application) {

    val presentationModule by lazy { PresentationModule(domainModule) }

    private val domainModule by lazy { DomainModule(dataModule) }

    private val dataModule by lazy { DataModule(application) }

    companion object {

        lateinit var INSTANCE: GlobalDI

        fun init(application: Application) {
            INSTANCE = GlobalDI(application)
        }
    }
}

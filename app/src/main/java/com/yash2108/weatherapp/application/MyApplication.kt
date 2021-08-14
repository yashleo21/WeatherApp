package com.yash2108.weatherapp.application

import android.app.Application
import com.yash2108.weatherapp.di.components.ApplicationComponent
import com.yash2108.weatherapp.di.components.DaggerApplicationComponent

class MyApplication: Application() {

    lateinit var appComponent: ApplicationComponent

    init {
        instance = this
    }

    override fun onCreate() {
       appComponent = DaggerApplicationComponent.factory().create(this)
        super.onCreate()
    }

    companion object {
        lateinit var instance: MyApplication

        fun getApplicationInstance() = instance
    }
}
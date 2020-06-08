package com.ninovanhooff.negenwnegenw

import android.app.Application
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        super.onCreate()

        injector = Injector(applicationContext)
    }

    override fun onTerminate() {
        super.onTerminate()
        Timber.v("onTerminate")
    }

    companion object {
        lateinit var injector: Injector
    }
}
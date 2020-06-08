package com.ninovanhooff.negenwnegenw

import android.content.Context

class Injector(private val appContext: Context) {

    fun provideContext(): Context = appContext
}
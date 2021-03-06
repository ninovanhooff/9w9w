package com.ninovanhooff.negenwnegenw

import android.content.Context
import com.ninovanhooff.negenwnegenw.data.Preferences
import com.ninovanhooff.negenwnegenw.services.WeatherService

class Injector(private val appContext: Context) {
    private val preferences: Preferences by lazy {
        Preferences(provideContext())
    }

    fun provideContext(): Context = appContext

    fun providePreferences(): Preferences = preferences

    fun provideWeatherService(): WeatherService = WeatherService.INSTANCE
}
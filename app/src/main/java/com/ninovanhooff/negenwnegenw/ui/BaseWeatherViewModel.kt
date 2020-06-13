package com.ninovanhooff.negenwnegenw.ui

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.ninovanhooff.negenwnegenw.MyApplication
import com.ninovanhooff.negenwnegenw.data.Preferences
import com.ninovanhooff.negenwnegenw.services.WeatherService
import com.ninovanhooff.negenwnegenw.services.dto.FiveDayForecastResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

abstract class BaseWeatherViewModel : ViewModel() {
    private val weather = WeatherService.INSTANCE
    private val prefs = MyApplication.injector.providePreferences()

    private val cityChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == Preferences.PREF_KEY_LAST_CITY_ID){
                loadForecast()
            }
        }


    init {
        prefs.registerListener(cityChangedListener)

        loadForecast()
    }

    protected fun loadForecast() {
        CoroutineScope(Dispatchers.IO).launch {
            val activeCity = prefs.getActiveCity()
            val response = weather.getForecast(activeCity.id)
            withContext(Dispatchers.Default) {
                // NOT run on main thread. dispatch / post updates to update UI.
                try {
                    if (response.isSuccessful) {
                        val body = response.body() ?: throw RuntimeException("Body is null")
                        handleForecastResponse(body)
                    } else {
                        throw HttpException(response)
                    }
                } catch (e: Throwable) {
                    handleResponseException(e)
                }
            }
        }
    }

    abstract fun handleForecastResponse(body: FiveDayForecastResponse)

    override fun onCleared() {
        super.onCleared()
        prefs.unRegisterListener(cityChangedListener)
    }

    private fun handleResponseException(e: Throwable) = Timber.e(e)

}
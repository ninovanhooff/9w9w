package com.ninovanhooff.negenwnegenw.ui.today

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ninovanhooff.negenwnegenw.MyApplication
import com.ninovanhooff.negenwnegenw.data.Preferences
import com.ninovanhooff.negenwnegenw.services.WeatherService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

class TodayViewModel : ViewModel() {
    private val weather = WeatherService.INSTANCE
    private val prefs = MyApplication.injector.providePreferences()

    // don't expose the mutable data...
    private val _forecastModel = MutableLiveData<ForecastModel>()
    // but the read-only data instead
    val forecastModel: LiveData<ForecastModel> = _forecastModel

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

    private fun loadForecast() {
        CoroutineScope(Dispatchers.IO).launch {
            val activeCity = prefs.getActiveCity()
            val response = weather.getForecast(activeCity.id)
            withContext(Dispatchers.Default) {
                // NOT run on main thread. dispatch / post updates to update UI.
                try {
                    if (response.isSuccessful) {
                        val timeSlot = response.body()?.timeSlots?.get(0)
                        if (timeSlot == null) {
                            Timber.e("Could not get timeslot from response $response")
                        } else {
                            _forecastModel.postValue(ForecastModel.fromTimeSlot(timeSlot))
                        }
                    } else {
                        throw HttpException(response)
                    }
                } catch (e: Throwable) {
                    handleResponseException(e)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        prefs.unRegisterListener(cityChangedListener)
    }

    private fun handleResponseException(e: Throwable) = Timber.e(e)

}
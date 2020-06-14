package com.ninovanhooff.negenwnegenw.ui.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ninovanhooff.negenwnegenw.services.dto.FiveDayForecastResponse
import com.ninovanhooff.negenwnegenw.ui.BaseWeatherViewModel
import com.ninovanhooff.negenwnegenw.ui.WeatherModel
import timber.log.Timber

class ForecastViewModel: BaseWeatherViewModel() {

    // don't expose the mutable data...
    private val _forecastPages = MutableLiveData<List<ForecastDoubleItem>>()
    // but the read-only data instead
    val forecastPages: LiveData<List<ForecastDoubleItem>> = _forecastPages

    init {
        super.loadForecast()
    }

    override fun handleForecastResponse(body: FiveDayForecastResponse) {
        // extract 4 daily forecasts from a list of timeslots

        val timeSlots = body.timeSlots

        if (timeSlots.size < 13){
            Timber.e("Insufficient timeslots for 4-day forecast")
            return
        }

        val unitString = prefs.getTemperatureUnit().toString()

        val items = listOf(
            ForecastDoubleItem(
                WeatherModel.fromTimeSlot(timeSlots[0], unitString),
                WeatherModel.fromTimeSlot(timeSlots[4], unitString)
            ),
            ForecastDoubleItem(
                WeatherModel.fromTimeSlot(timeSlots[8], unitString),
                WeatherModel.fromTimeSlot(timeSlots[12], unitString)
            )
        )

        _forecastPages.postValue(items)
    }
}
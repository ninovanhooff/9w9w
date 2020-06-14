package com.ninovanhooff.negenwnegenw.ui.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ninovanhooff.negenwnegenw.services.dto.FiveDayForecastResponse
import com.ninovanhooff.negenwnegenw.ui.BaseWeatherViewModel
import com.ninovanhooff.negenwnegenw.ui.WeatherModel

/** Provides [forecastPages] which contain two weather forecasts: [WeatherModel] each */
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

        val unitString = prefs.getTemperatureUnit().toString()
        val tzOffsetSeconds = body.city.timezone

        // Break timeslots into pages of 4 items
        val subLists = timeSlots.windowed(4, 4, partialWindows = false)
        // Discard very odd-numbered 3-hour interval, resulting in 2 forecasts per day and
        // a total of 10 pages for a FiveDayForecastResponse
        val items = subLists.map {
            ForecastDoubleItem(
                WeatherModel.fromTimeSlot(it[0], unitString, tzOffsetSeconds),
                WeatherModel.fromTimeSlot(it[2], unitString, tzOffsetSeconds)
            )
            // discard indexes 1 and 3
        }

        _forecastPages.postValue(items)
    }
}
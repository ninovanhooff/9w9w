package com.ninovanhooff.negenwnegenw.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ninovanhooff.negenwnegenw.services.dto.FiveDayForecastResponse
import com.ninovanhooff.negenwnegenw.ui.BaseWeatherViewModel
import com.ninovanhooff.negenwnegenw.ui.WeatherModel

class TodayViewModel : BaseWeatherViewModel() {

    // don't expose the mutable data...
    private val _forecastModel = MutableLiveData<WeatherModel>()
    // but the read-only data instead
    val weatherModel: LiveData<WeatherModel> = _forecastModel


    init {
        super.loadForecast()
    }

    override fun handleForecastResponse(body: FiveDayForecastResponse) {
        if (body.timeSlots.isEmpty()){
            return
        }
        val timeSlot = body.timeSlots[0]
        val tempUnit = prefs.getTemperatureUnit().toString()
        _forecastModel.postValue(WeatherModel.fromTimeSlot(timeSlot, tempUnit))
    }

}
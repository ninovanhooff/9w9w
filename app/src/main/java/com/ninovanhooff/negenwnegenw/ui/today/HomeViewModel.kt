package com.ninovanhooff.negenwnegenw.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ninovanhooff.negenwnegenw.services.WeatherService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class TodayViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is today Fragment"
    }
    val text: LiveData<String> = _text

    init {
        val weather = WeatherService.INSTANCE

        CoroutineScope(Dispatchers.IO).launch {
            val response = weather.getForecast()
            withContext(Dispatchers.Default) {
                // NOT run on main thread. dispatch / post updates to update UI.
                try {
                    if (response.isSuccessful) {
                        val temperatureString =
                            response.body()?.timeSlots?.get(0)?.main?.temp.toString()
                        _text.postValue(temperatureString)
                    } else {
                        _text.postValue("Error: ${response.code()}")
                    }
                } catch (e: HttpException) {
                    _text.postValue("Exception ${e.message}")
                } catch (e: Throwable) {
                    _text.postValue("Ooops: Something else went wrong")
                }
            }
        }
    }
}
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
import timber.log.Timber

class TodayViewModel : ViewModel() {

    // don't expose the mutable data...
    private val _todayModel = MutableLiveData<TodayModel>()
    // but the read-only data instead
    val todayModel: LiveData<TodayModel> = _todayModel

    init {
        val weather = WeatherService.INSTANCE

        CoroutineScope(Dispatchers.IO).launch {
            val response = weather.getForecast()
            withContext(Dispatchers.Default) {
                // NOT run on main thread. dispatch / post updates to update UI.
                try {
                    if (response.isSuccessful) {
                        val timeSlot = response.body()?.timeSlots?.get(0)
                        if (timeSlot == null){
                            Timber.e("Could not get timeslot from response $response")
                        } else {
                            _todayModel.postValue(TodayModel.fromTimeSlot(timeSlot))
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

    private fun handleResponseException(e: Throwable) = Timber.e(e)

}
package com.ninovanhooff.negenwnegenw.ui.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ninovanhooff.negenwnegenw.BuildConfig
import com.ninovanhooff.negenwnegenw.services.WeatherService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TodayViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is today Fragment"
    }
    val text: LiveData<String> = _text

    init {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor { chain ->
            val original = chain.request()

            // Request customization: add request headers
            val requestBuilder = original.newBuilder()
                .addHeader("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
                .addHeader("x-rapidapi-key", BuildConfig.rapidapi_key)

            val request = requestBuilder.build()
            chain.proceed(request)
        }

        val okHttp = httpClient.build()

        val retrofit = Retrofit.Builder()
            .client(okHttp)
            .baseUrl("https://community-open-weather-map.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: WeatherService = retrofit.create(WeatherService::class.java)




        CoroutineScope(Dispatchers.IO).launch {
            val response = service.getForecast()
            withContext(Dispatchers.Default) {
                // NOT run on main thread. dispatch / post updates to update UI.
                try {
                    if (response.isSuccessful) {
                        _text.postValue("yay")
                        //Do something with response e.g show to the UI.
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
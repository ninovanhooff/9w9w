package com.ninovanhooff.negenwnegenw.services

import com.ninovanhooff.negenwnegenw.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface WeatherService {

    @GET("forecast/?q=san francisco")
    suspend fun getForecast(): Response<FiveDayForecastResponse>

    companion object {
        val INSTANCE: WeatherService by lazy {
            createInstance()
        }


        private fun createInstance(): WeatherService {
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

            return retrofit.create(WeatherService::class.java)
        }
    }
}

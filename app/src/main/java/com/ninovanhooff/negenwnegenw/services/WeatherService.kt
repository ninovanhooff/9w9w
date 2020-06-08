package com.ninovanhooff.negenwnegenw.services

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers


interface WeatherService {

    @GET("forecast/?q=san francisco")
    suspend fun getForecast(): Response<FiveDayForecastResponse>

}

class FiveDayForecastResponse(
    val list: List<OneDayForecast>
) : BaseApiResponse()

data class OneDayForecast(val dt: Long)

abstract class BaseApiResponse {
    val cod: Int = 0
    val message: String? = null
}
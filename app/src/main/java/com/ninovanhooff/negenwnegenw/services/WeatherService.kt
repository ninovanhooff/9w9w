package com.ninovanhooff.negenwnegenw.services

import com.ninovanhooff.negenwnegenw.MyApplication
import com.ninovanhooff.negenwnegenw.services.Interceptors.defaultCacheControlInterceptor
import com.ninovanhooff.negenwnegenw.services.Interceptors.rapidApiAuthorizationInterceptor
import com.ninovanhooff.negenwnegenw.services.dto.FindResponse
import com.ninovanhooff.negenwnegenw.services.dto.FiveDayForecastResponse
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File

/** OpenWeatherMap API endpoint. Responses may be cached. See [Interceptors.defaultCacheControl] */
interface WeatherService {

    /** Get a 5-day forecast, subdivided in 3-hour intervals */
    @GET("forecast")
    suspend fun getForecast(@Query("id") cityId: Int, @Query("units") units: String): Response<FiveDayForecastResponse>

    /** Search matching cities given substring query [query].
     * The documented `type=like` query param for substring search seems to be ineffective */
    @GET("find?type=like")
    suspend fun getCities(@Query("q") query: String): Response<FindResponse>

    companion object {
        private const val CACHE_SIZE = (5 * 1024 * 1024).toLong()
        private const val CACHE_DIR_RELATIVE_PATH = "weather_service_cache"

        val INSTANCE: WeatherService by lazy {
            createInstance()
        }

        private fun createInstance(): WeatherService {
            val cacheDir = File(
                MyApplication.injector.provideContext().cacheDir,
                CACHE_DIR_RELATIVE_PATH
            )
            cacheDir.mkdir()
            val myCache = Cache(cacheDir, CACHE_SIZE)

            val okHttp = OkHttpClient.Builder()
                .cache(myCache)
                .addNetworkInterceptor(rapidApiAuthorizationInterceptor)
                .addNetworkInterceptor(defaultCacheControlInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .client(okHttp)
                .baseUrl("https://community-open-weather-map.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(WeatherService::class.java)
        }
    }
}

package com.ninovanhooff.negenwnegenw.services

import com.ninovanhooff.negenwnegenw.BuildConfig
import com.ninovanhooff.negenwnegenw.MyApplication
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.io.File
import java.util.concurrent.TimeUnit


interface WeatherService {

    @GET("forecast/?q=utrecht&units=metric")
    suspend fun getForecast(): Response<FiveDayForecastResponse>

    companion object {
        private const val CACHE_SIZE = (5 * 1024 * 1024).toLong()
        private const val CACHE_DIR_RELATIVE_PATH = ""

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

            val defaultCacheControl = CacheControl.Builder()
                .maxAge(15, TimeUnit.MINUTES) // 15 minutes cache
                .build()


            val httpClient = OkHttpClient.Builder()
            httpClient.cache(myCache)
            httpClient.addInterceptor { chain ->
                val original = chain.request()

                // Request customization: add request headers
                val requestBuilder = original.newBuilder()
                    .addHeader("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", BuildConfig.rapidapi_key)

                // Use a default caching policy if not provided
                if (original.header("Cache-Control").isNullOrBlank()) {
                    requestBuilder.addHeader("Cache-Control", defaultCacheControl.toString())
                }

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

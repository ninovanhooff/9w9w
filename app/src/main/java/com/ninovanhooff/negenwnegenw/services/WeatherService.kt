package com.ninovanhooff.negenwnegenw.services

import com.ninovanhooff.negenwnegenw.BuildConfig
import com.ninovanhooff.negenwnegenw.MyApplication
import com.ninovanhooff.negenwnegenw.services.dto.FindResponse
import com.ninovanhooff.negenwnegenw.services.dto.FiveDayForecastResponse
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File
import java.util.concurrent.TimeUnit


interface WeatherService {

    @GET("forecast?units=metric")
    suspend fun getForecast(@Query("id") cityId: Int): Response<FiveDayForecastResponse>

    /** The documented `type=like` query param for substring search seems to be ineffective */
    @GET("find?type=like")
    suspend fun getCities(@Query("q") query: String): Response<FindResponse>

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
                .maxAge(30, TimeUnit.MINUTES)
                .build()
                .toString()


            val httpClient = OkHttpClient.Builder()
            httpClient.cache(myCache)
            httpClient.addNetworkInterceptor { chain ->
                val originalRequest = chain.request()

                // Request customization: add request headers
                val requestBuilder = originalRequest.newBuilder()
                    .addHeader("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", BuildConfig.rapidapi_key)

                val request = requestBuilder.build()

                chain.proceed(request)
            }

            /** Dangerous interceptor that rewrites the server's cache-control header. */
            val rewriteCacheControlInterceptor: Interceptor = object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                    val originalResponse = chain.proceed(chain.request())

                    return originalResponse.newBuilder()
                        .header("Cache-Control", defaultCacheControl)
                        .build()
                }
            }

            httpClient.addNetworkInterceptor(rewriteCacheControlInterceptor)

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

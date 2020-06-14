package com.ninovanhooff.negenwnegenw.services

import com.ninovanhooff.negenwnegenw.BuildConfig
import okhttp3.CacheControl
import okhttp3.Interceptor
import java.util.concurrent.TimeUnit

object Interceptors {
    private val defaultCacheControl = CacheControl.Builder()
        .maxAge(30, TimeUnit.MINUTES)
        .build()
        .toString()

    /** Adds [defaultCacheControl] if the response doesn't provide 'Cache-Control` */
    val defaultCacheControlInterceptor = Interceptor { chain ->
        val originalResponse = chain.proceed(chain.request())

        if (originalResponse.header("Cache-Control").isNullOrBlank()) {
            originalResponse.newBuilder()
                .header("Cache-Control", defaultCacheControl)
                .build()
        } else {
            originalResponse
        }
    }

    /** Injects the RapidApi authorization headers into requests
     * Api key is loaded from the secrets.properties file */
    val rapidApiAuthorizationInterceptor: (Interceptor.Chain) -> okhttp3.Response = { chain ->
        val originalRequest = chain.request()

        // Request customization: add request headers
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com")
            .addHeader("x-rapidapi-key", BuildConfig.rapidapi_key)

        val request = requestBuilder.build()

        chain.proceed(request)
    }
}
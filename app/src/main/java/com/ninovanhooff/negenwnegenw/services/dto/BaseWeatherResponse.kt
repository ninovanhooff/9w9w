package com.ninovanhooff.negenwnegenw.services.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

abstract class BaseApiResponse {
    val cod: Int = 0
    val message: String? = null
}

// Models shared between several OpenWeatherMap Responses

data class Main(
    @Expose
    @SerializedName("temp")
    val temp: Double,
    @Expose
    @SerializedName("feels_like")
    val feels_like: Double,
    @Expose
    @SerializedName("temp_min")
    val temp_min: Double,
    @Expose
    @SerializedName("temp_max")
    val temp_max: Double,
    @Expose
    @SerializedName("pressure")
    val pressure: Int,
    @Expose
    @SerializedName("sea_level")
    val sea_level: Int,
    @Expose
    @SerializedName("grnd_level")
    val grnd_level: Int,
    @Expose
    @SerializedName("humidity")
    val humidity: Int,
    @Expose
    @SerializedName("temp_kf")
    val temp_kf: Double
)

data class Wind(
    @Expose
    @SerializedName("speed")
    val speed: Double,
    @Expose
    @SerializedName("deg")
    val deg: Int
)

data class Clouds(
    @Expose
    @SerializedName("all")
    val all: Int
)

data class Weather(
    @Expose
    @SerializedName("id")
    val id: Int,
    @Expose
    @SerializedName("main")
    val main: String,
    @Expose
    @SerializedName("description")
    val description: String,
    @Expose
    @SerializedName("icon")
    val icon: String
)
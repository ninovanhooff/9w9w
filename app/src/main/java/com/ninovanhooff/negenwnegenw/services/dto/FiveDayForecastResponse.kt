package com.ninovanhooff.negenwnegenw.services.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FiveDayForecastResponse(
    @Expose
    @SerializedName("list")
    val timeSlots: List<TimeSlot>,
    @Expose
    @SerializedName("city")
    val city: City
) : BaseApiResponse()

data class TimeSlot(
    @Expose
    @SerializedName("dt")
    val dt: Int,
    @Expose
    @SerializedName("main")
    val main: Main,
    @Expose
    @SerializedName("weather")
    val weather: List<Weather>,
    @Expose
    @SerializedName("clouds")
    val clouds: Clouds,
    @Expose
    @SerializedName("wind")
    val wind: Wind,
    @Expose
    @SerializedName("rain")
    val rain: Rain,
    @Expose
    @SerializedName("sys")
    val sys: FiveDaySys,
    @Expose
    @SerializedName("dt_txt")
    val dt_txt: String
)

data class FiveDaySys(
    @Expose
    @SerializedName("pod")
    val pod: String
)

data class Rain(
    @Expose
    @SerializedName("3h")
    val amount: Double
)

data class City(
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("country")
    val country: String,
    @Expose
    @SerializedName("id")
    val id: Int
)






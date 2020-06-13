package com.ninovanhooff.negenwnegenw.services.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class FiveDayForecastResponse(
    @Expose
    @SerializedName("list")
    val timeSlots: List<TimeSlot>
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
    val sys: Sys,
    @Expose
    @SerializedName("dt_txt")
    val dt_txt: String
)

data class Sys(
    @Expose
    @SerializedName("pod")
    val pod: String
)

data class Rain(
    @Expose
    @SerializedName("3h")
    val amount: Double
)






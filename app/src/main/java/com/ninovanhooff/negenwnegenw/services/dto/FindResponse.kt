package com.ninovanhooff.negenwnegenw.services.dto

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class FindResponse(
    @Expose
    @SerializedName("list")
    val cities: List<CityResult>
) : BaseApiResponse()

data class CityResult(
    @Expose
    @SerializedName("id")
    var id: Int,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("coord")
    var coord: Coord,
    @Expose
    @SerializedName("main")
    var main: Main,
    @Expose
    @SerializedName("dt")
    var dt: Int,
    @Expose
    @SerializedName("wind")
    var wind: Wind,
    @Expose
    @SerializedName("sys")
    var sys: CitySys,
    @Expose
    @SerializedName("clouds")
    var clouds: Clouds,
    @Expose
    @SerializedName("weather")
    var weather: List<Weather>
)

data class CitySys(
    @Expose
    @SerializedName("country")
    val country: String
)

data class Coord(
    @Expose
    @SerializedName("lat")
    val lat: Double,
    @Expose
    @SerializedName("lon")
    val lon: Double
)
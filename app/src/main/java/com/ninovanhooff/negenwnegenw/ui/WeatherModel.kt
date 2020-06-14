package com.ninovanhooff.negenwnegenw.ui

import android.annotation.SuppressLint
import androidx.annotation.RawRes
import com.ninovanhooff.negenwnegenw.R
import com.ninovanhooff.negenwnegenw.services.dto.TimeSlot
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/** A model for data-binding fragment_today.xml
 * Because all numbers get bound to TextViews, we use String as data type */
data class WeatherModel(
    val temp: String,
    val tempUnit: String,
    val feelsLike: String,
    val tempMinMax: String,
    val weatherDescription: String,
    @RawRes val animationRawRes: Int,
    val dateTime: String
){

    override fun toString(): String {
        return "$temp ($weatherDescription)"
    }

    companion object {
        /** Maps OpenWeather Main labels to Lottie animations.
         * For a more precise (and possibly more stable) classification, id codes could be used
         * https://openweathermap.org/weather-conditions#Weather-Condition-Codes
         */
        private val lottieMap = mapOf(
            "Clear" to R.raw.lottie_sunny,
            "Clouds" to R.raw.lottie_windy,
            "Thunderstorm" to R.raw.lottie_thunderstorm,
            "Rain" to R.raw.lottie_partly_shower
        )

        @SuppressLint("DefaultLocale") // using Locale requires experimental kotlin overload
        fun fromTimeSlot(timeSlot: TimeSlot, tempUnit: String, tzOffsetSeconds: Int): WeatherModel {
            val main = timeSlot.main
            val tempMinMax =
                "Min: ${main.temp_min.convertTemp()}° ↓ · " +
                "Max: ${main.temp_max.convertTemp()}° ↑"

            val tz = TimeZone.getDefault()
            tz.rawOffset = tzOffsetSeconds * 1000
            val sdf = SimpleDateFormat("EEEE h a", Locale.getDefault())
            val dateFormat = Date((timeSlot.dt  + tzOffsetSeconds) * 1000L)
            val weekday: String = sdf.format(dateFormat)
            return WeatherModel(
                main.temp.convertTemp(),
                tempUnit,
                main.feels_like.convertTemp(),
                tempMinMax,
                timeSlot.weather[0].description.capitalize(),
                lottieMap[timeSlot.weather[0].main]
                    ?: R.raw.lottie_partly_cloudy,
                weekday
            )
        }

        private fun Double.convertTemp() = this.roundToInt().toString()
    }
}

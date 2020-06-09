package com.ninovanhooff.negenwnegenw.ui.today

import androidx.annotation.RawRes
import com.ninovanhooff.negenwnegenw.R
import com.ninovanhooff.negenwnegenw.services.TimeSlot
import kotlin.math.roundToInt

/** A model for data-binding fragment_today.xml
 * Because all numbers get bound to TextViews, we use String as data type */
data class TodayModel(
    val temp: String,
    val tempUnit: String,
    val feelsLike: String,
    val tempMin: String, val tempMax: String,
    val weatherDescription: String,
    @RawRes val animationRawRes: Int
){
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

        fun fromTimeSlot(timeSlot: TimeSlot): TodayModel {
            return TodayModel(
                timeSlot.main.temp.convertTemp(),
                "℃",
                timeSlot.main.feels_like.convertTemp(),
                timeSlot.main.temp_min.convertTemp(),
                timeSlot.main.temp_max.convertTemp(),
                timeSlot.weather[0].description,
                lottieMap[timeSlot.weather[0].main] ?: R.raw.lottie_partly_cloudy
            )
        }

        private fun Double.convertTemp() = this.roundToInt().toString()
    }
}

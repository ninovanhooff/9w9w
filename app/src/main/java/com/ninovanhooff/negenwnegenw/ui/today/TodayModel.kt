package com.ninovanhooff.negenwnegenw.ui.today

import com.ninovanhooff.negenwnegenw.services.TimeSlot
import kotlin.math.roundToInt

/** A model for data-binding fragment_today.xml
 * Because all numbers get bound to TextViews, we use String as data type */
data class TodayModel(
    val temp: String,
    val tempUnit: String,
    val feelsLike: String,
    val tempMin: String, val tempMax: String,
    val weatherDescription: String
){
    companion object {
        fun fromTimeSlot(timeSlot: TimeSlot): TodayModel {
            return TodayModel(
                timeSlot.main.temp.convertTemp(),
                "℃",
                timeSlot.main.feels_like.convertTemp(),
                timeSlot.main.temp_min.convertTemp(),
                timeSlot.main.temp_max.convertTemp(),
                timeSlot.weather[0].description
            )
        }

        private fun Double.convertTemp() = this.roundToInt().toString()
    }
}

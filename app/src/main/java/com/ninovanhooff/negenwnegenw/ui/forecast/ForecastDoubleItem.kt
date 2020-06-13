package com.ninovanhooff.negenwnegenw.ui.forecast

import com.ninovanhooff.negenwnegenw.ui.WeatherModel

data class ForecastDoubleItem(val left: WeatherModel, val right: WeatherModel) {

    override fun toString(): String = "($left, $right)"
}

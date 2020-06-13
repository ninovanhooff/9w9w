package com.ninovanhooff.negenwnegenw.ui.forecast

import com.ninovanhooff.negenwnegenw.ui.ForecastModel

data class ForecastDoubleItem(val id: String, val left: ForecastModel, val right: ForecastModel) {

    override fun toString(): String = "($left, $right)"
}

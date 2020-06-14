package com.ninovanhooff.negenwnegenw.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.ninovanhooff.negenwnegenw.CityModel

/** SharedPreferences Implementation. Stores user preferences. */
class Preferences constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    /** Warning: keep a strong reference to the listener and call unRegisterListener
     * when you don't need updates anymore */
    fun registerListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) =
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

    fun unRegisterListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
    }

    /** Name of the last viewed city */
    fun getActiveCityName(): String {
        return sharedPreferences
            .getString(PREF_KEY_LAST_CITY_NAME, null)
            ?: DEFAULT_LAST_CITY_NAME

    }

    fun setActiveCityName(name: String) {
        with(sharedPreferences.edit()) {
            putString(PREF_KEY_LAST_CITY_NAME, name)
            apply()
        }
    }

    /** OpenWeatherMap API city id of the last viewed city */
    fun getActiveCityId(): Int {
        return sharedPreferences.getInt(PREF_KEY_LAST_CITY_ID, DEFAULT_LAST_CITY_ID)
    }

    fun setActiveCityId(id: Int) {
        with(sharedPreferences.edit()) {
            putInt(PREF_KEY_LAST_CITY_ID, id)
            apply()
        }
    }

    fun getActiveCityCountryCode(): String {
        return sharedPreferences
            .getString(PREF_KEY_LAST_CITY_COUNTRY_CODE, null)
            ?: DEFAULT_LAST_CITY_COUNTRY_CODE
    }

    fun setActiveCityCountryCode(countryCode: String) {
        with(sharedPreferences.edit()) {
            putString(PREF_KEY_LAST_CITY_COUNTRY_CODE, countryCode)
            apply()
        }
    }

    fun getActiveCity(): CityModel {
        return CityModel(
            getActiveCityName(),
            getActiveCityCountryCode(),
            getActiveCityId()
        )
    }

    fun setActiveCity(model: CityModel) {
        setActiveCityName(model.name)
        setActiveCityCountryCode(model.countryCode)
        setActiveCityId(model.id)
    }

    fun getTemperatureUnit(): TemperatureUnit {
        return TemperatureUnit
            .getByValue(sharedPreferences.getInt(PREF_KEY_TEMPERATURE_UNIT, DEFAULT_TEMPERATURE_UNIT.value))
            ?: DEFAULT_TEMPERATURE_UNIT
    }

    fun setTemperatureUnit(unit: TemperatureUnit) {
        with(sharedPreferences.edit()) {
            putInt(PREF_KEY_TEMPERATURE_UNIT, unit.value)
            apply()
        }
    }

    /** Changes temperature units between Celsius and Fahrenheit */
    fun toggleTemperatureUnit() {
        if (getTemperatureUnit() == TemperatureUnit.CELSIUS){
            setTemperatureUnit(TemperatureUnit.FAHRENHEIT)
        } else {
            setTemperatureUnit(TemperatureUnit.CELSIUS)
        }
    }

    companion object {

        // Change first-start defaults below
        const val PREF_KEY_LAST_CITY_NAME = "pref_key_last_city_name"
        const val DEFAULT_LAST_CITY_NAME = "Utrecht"
        const val PREF_KEY_LAST_CITY_ID = "pref_key_last_city_id"
        const val DEFAULT_LAST_CITY_ID = 2745912 // Utrecht city, NL
        const val PREF_KEY_LAST_CITY_COUNTRY_CODE = "pref_key_last_city_country_code"
        const val DEFAULT_LAST_CITY_COUNTRY_CODE = "NL"
        const val PREF_KEY_TEMPERATURE_UNIT = "pref_key_temperature_unit"
        val DEFAULT_TEMPERATURE_UNIT = TemperatureUnit.CELSIUS
    }

    enum class TemperatureUnit(val value: Int){
        CELSIUS(0),
        FAHRENHEIT(1);

        companion object {
            private val values = values()
            fun getByValue(value: Int) = values.firstOrNull { it.value == value }
        }

    }
}
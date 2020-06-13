package com.ninovanhooff.negenwnegenw.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

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

    companion object {

        // Change first-start city defaults below
        const val PREF_KEY_LAST_CITY_NAME = "pref_key_last_city_name"
        const val DEFAULT_LAST_CITY_NAME = "Utrecht"
        const val PREF_KEY_LAST_CITY_ID = "pref_key_last_city_id"
        const val DEFAULT_LAST_CITY_ID = 2745912 // Utrecht city, NL
        const val PREF_KEY_LAST_CITY_COUNTRY_CODE = "pref_last_city_country_code"
        const val DEFAULT_LAST_CITY_COUNTRY_CODE = "NL"
    }
}
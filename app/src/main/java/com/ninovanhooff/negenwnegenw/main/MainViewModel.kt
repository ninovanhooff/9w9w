package com.ninovanhooff.negenwnegenw.main

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ninovanhooff.negenwnegenw.MyApplication
import com.ninovanhooff.negenwnegenw.data.Preferences
import com.ninovanhooff.negenwnegenw.services.WeatherService
import com.ninovanhooff.negenwnegenw.util.debounce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

class MainViewModel: ViewModel() {
    private val weather = MyApplication.injector.provideWeatherService()
    private val prefs = MyApplication.injector.providePreferences()

    private val _citySuggestions = MutableLiveData<List<CityModel>>()
    val citySuggestions: LiveData<List<CityModel>> = _citySuggestions

    private val _cityModel = MutableLiveData(prefs.getActiveCity())
    val cityModel: LiveData<CityModel> = _cityModel

    private val cityChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == Preferences.PREF_KEY_LAST_CITY_ID){
                _cityModel.postValue(prefs.getActiveCity())
            }
        }

    /** Callback for when the user changed the city input field.
     *  May be safely called for every character input, api access is throttled */
    val onCityInputChange: (String) -> Unit =
        debounce(300L, viewModelScope, this::performCitySearch)

    init {
        prefs.registerListener(cityChangedListener)
    }

    override fun onCleared() {
        super.onCleared()
        prefs.unRegisterListener(cityChangedListener)
    }


    /** Query the [WeatherService] for cities containing [query].
     *
     *  Observe [citySuggestions] for results
     *  @param selectFirst: when the search completes, select the first result and
     *  update the active Fragment for that city
     *
     */
    private fun performCitySearch(query: String, selectFirst: Boolean = false) {
        if (query.length < 3){
            // minimum query length is 3
            //todo discuss: Where should this check be placed?
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val response = weather.getCities(query)
            withContext(Dispatchers.Default) {
                // NOT run on main thread. dispatch / post updates to update UI.
                try {
                    if (response.isSuccessful) {
                        val suggestions = response.body()?.cities?.map {
                            CityModel(
                                it.name,
                                it.sys.country,
                                it.id
                            )
                        } ?: listOf()
                        if(suggestions.isNotEmpty() && selectFirst){
                            prefs.setActiveCity(suggestions.first())
                        } else {
                            _citySuggestions.postValue(suggestions)
                        }
                    } else {
                        throw HttpException(response)
                    }
                } catch (e: Throwable) {
                    Timber.e(e)
                }
            }
        }
    }



    /** Search for [query] and auto-select the first result */
    fun onCityInputSubmit(query: String) {
        performCitySearch(query, true)
    }

    fun onCitySuggestionSelected(cityModel: CityModel) {
        prefs.setActiveCity(cityModel)
    }

    /** Explicitly-declared overload to support single parameter usage required by debounce
     * Apparently Kotlin 1.3 feature "Function reference with default value as other type"
     * is disabled. */
    private fun performCitySearch(query: String){
        performCitySearch(query, selectFirst = false)
    }

    fun onFabClicked() {
        prefs.toggleTemperatureUnit()
    }
}


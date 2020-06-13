package com.ninovanhooff.negenwnegenw

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.ninovanhooff.negenwnegenw.services.WeatherService
import com.ninovanhooff.negenwnegenw.util.debounce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber

class MainViewModel: ViewModel() {

    private val _citySuggestions = MutableLiveData<List<MySuggestion>>()
    val citySuggestions: LiveData<List<MySuggestion>> = _citySuggestions

    private val weather = WeatherService.INSTANCE

    /** Callback for when the user changed the city input field.
     *  May be safely called for every character input, api access is throttled */
    val onCityInputChange: (String) -> Unit =
        debounce(300L, viewModelScope, this::performCitySearch)


    /** Query the [WeatherService] for cities containing [newQuery].
     *
     *  Observe [citySuggestions] for results */
    private fun performCitySearch(newQuery: String) {
        if (newQuery.length < 3){
            // minimum query length is 3
            //todo discuss: Where should this check be placed?
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val response = weather.getCities(newQuery)
            withContext(Dispatchers.Default) {
                // NOT run on main thread. dispatch / post updates to update UI.
                try {
                    if (response.isSuccessful) {
                        val suggestions = response.body()?.cities?.map {
                            MySuggestion(it.name)
                        } ?: listOf()
                        _citySuggestions.postValue(suggestions)
                    } else {
                        throw HttpException(response)
                    }
                } catch (e: Throwable) {
                    Timber.e(e)
                }
            }
        }
    }
}

class MySuggestion(private val body: String?) : SearchSuggestion {
    constructor(parcel: Parcel) : this(parcel.readString())

    override fun getBody(): String? {
        return body
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(body)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MySuggestion> {
        override fun createFromParcel(parcel: Parcel): MySuggestion {
            return MySuggestion(parcel)
        }

        override fun newArray(size: Int): Array<MySuggestion?> {
            return arrayOfNulls(size)
        }
    }

}
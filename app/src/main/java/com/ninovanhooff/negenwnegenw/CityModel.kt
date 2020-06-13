package com.ninovanhooff.negenwnegenw

import android.os.Parcel
import android.os.Parcelable
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.ninovanhooff.negenwnegenw.data.Preferences

class CityModel(val name: String, val countryCode: String, val id: Int) :
    SearchSuggestion {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt()
    )

    override fun getBody(): String {
        return "$name ($countryCode)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        fun Preferences.getActiveCity(): CityModel {
            return CityModel(
                getActiveCityName(),
                getActiveCityCountryCode(),
                getActiveCityId()
            )
        }

        fun Preferences.setActiveCity(model: CityModel) {
            setActiveCityName(model.name)
            setActiveCityCountryCode(model.countryCode)
            setActiveCityId(model.id)
        }

        @JvmField
        val CREATOR = object : Parcelable.Creator<CityModel> {
            override fun createFromParcel(parcel: Parcel): CityModel {
                return CityModel(parcel)
            }

            override fun newArray(size: Int): Array<CityModel?> {
                return arrayOfNulls(size)
            }

        }
    }



}
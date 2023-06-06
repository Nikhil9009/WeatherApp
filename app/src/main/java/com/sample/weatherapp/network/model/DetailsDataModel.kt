package com.sample.weatherapp.network.model

import android.os.Parcel
import android.os.Parcelable

data class DetailsDataModel(
    val temperature: String? = null,
    val feelsLikeTemperature: String? = null,
    val main: String? = null,
    val description: String? = null,
    val screenTitle: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(temperature)
        parcel.writeString(feelsLikeTemperature)
        parcel.writeString(main)
        parcel.writeString(description)
        parcel.writeString(screenTitle)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DetailsDataModel> {
        override fun createFromParcel(parcel: Parcel): DetailsDataModel {
            return DetailsDataModel(parcel)
        }

        override fun newArray(size: Int): Array<DetailsDataModel?> {
            return arrayOfNulls(size)
        }
    }
}
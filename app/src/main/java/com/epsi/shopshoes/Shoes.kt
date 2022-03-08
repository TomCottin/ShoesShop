package com.epsi.shopshoes

import android.os.Parcel
import android.os.Parcelable

class Shoes(
        val name: String ?= null,
        val price: String ?= null,
        val description: String ?= null,
        val image: String ?= null,
        var quantity: String ?= null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(price)
        parcel.writeString(description)
        parcel.writeString(image)
        parcel.writeString(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Shoes> {
        override fun createFromParcel(parcel: Parcel): Shoes {
            return Shoes(parcel)
        }

        override fun newArray(size: Int): Array<Shoes?> {
            return arrayOfNulls(size)
        }
    }
}
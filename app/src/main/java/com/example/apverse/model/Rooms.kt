package com.example.apverse.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rooms(
    val room_id: String = "",
    val capacity: String = ""
):Parcelable
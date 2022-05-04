package com.example.apverse.model

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize

//data class Rooms(
//    val stringResourceId: Int,
//    val capacity: Int
//)

@Parcelize
data class Rooms(
    val room_id: String = "",
    val capacity: String = ""
):Parcelable
package com.example.apverse.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RoomBooking(
    val room_id: String = "",
    val student_email: String = "",
    val student_name: String = "",
    val student_tp: String = "",
    val date: String = "",
    val time: String = "",
    val hdmi_cable: Boolean = false
): Parcelable

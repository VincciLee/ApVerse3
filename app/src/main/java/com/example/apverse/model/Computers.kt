package com.example.apverse.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Computers(
    var doc_id:String = "",
    val computer_id: String = "",
    val computer_status: String = "",
    val student_email: String = ""
): Parcelable

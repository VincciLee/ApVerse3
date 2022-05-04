package com.example.apverse.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Users(
    val user_email: String = "",
    val user_name: String = "",
    val user_type: String = "",
    val user_profile: String = ""
):Parcelable

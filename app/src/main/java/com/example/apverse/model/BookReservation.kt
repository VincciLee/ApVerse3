package com.example.apverse.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BookReservation(
    var doc_id: String = "",
    val book_id: String = "",
    val date: String = "",
    var ready: Boolean = false,
    val reservation_status: String = "",
    val student_email: String = "",
    val student_name: String = "",
    val time: String = ""
): Parcelable

@Parcelize
data class NewBookReservation(
    val book_id: String = "",
    val date: String = "",
    var ready: Boolean = false,
    val reservation_status: String = "",
    val student_email: String = "",
    val student_name: String = "",
    val time: String = ""
): Parcelable

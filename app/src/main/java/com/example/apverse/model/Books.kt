package com.example.apverse.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Books(
    var doc_id:String = "",
    val book_id:String = "",
    val book_title:String = "",
    val book_author:String = "",
    val book_year:String = "",
    val book_status:String = "",
    val book_image:String = ""
): Parcelable

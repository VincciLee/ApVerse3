package com.example.apverse.ui.librarian.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.RoomBooking
import java.text.SimpleDateFormat
import java.util.*

class LHomeViewModel : ViewModel() {
    var bookingCount = 0
    var reservationCount = 0

    init {
        Log.i("ApVerse::LHomeVM", "init()")
        val current = Calendar.getInstance().time
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        val today = dateFormatter.format(current)

//        FirestoreClass().getBookingCount(this, today)
    }

    fun getBookingCount(bookingList: ArrayList<RoomBooking>){
        Log.i("ApVerse::LHomeVM", "get bookingCount")
        bookingCount = bookingList.size
    }

    private fun getReservationCount(){

    }
}
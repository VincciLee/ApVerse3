package com.example.apverse.ui.librarian.home

import androidx.lifecycle.ViewModel
import com.example.apverse.firestore.Firestore
import com.example.apverse.model.BookReservation
import com.example.apverse.model.RoomBooking

class LHomeViewModel : ViewModel() {

    var roomBookingList: ArrayList<RoomBooking> = ArrayList()
    var roomBookingCount: Int = 0
    var bookReservationList: ArrayList<BookReservation> = ArrayList()
    var bookReservationCount: Int = 0

    suspend fun getRoomBookingCount(date: String) {
        roomBookingList.clear()

        val data = Firestore().getBookingCount(date)
        for (i in data?.documents!!){
            val booking = i?.toObject(RoomBooking::class.java)!!
            roomBookingList.add(booking)
        }

        roomBookingCount = roomBookingList.size
    }

    suspend fun getBookReservationCount() {
        bookReservationList.clear()

        val data = Firestore().getBookReservationCount()
        for (i in data?.documents!!){
            val reservation = i?.toObject(BookReservation::class.java)!!
            bookReservationList.add(reservation)
        }

        bookReservationCount = bookReservationList.size
    }
}
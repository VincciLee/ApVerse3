package com.example.apverse.ui.student.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.apverse.firestore.Firestore
import com.example.apverse.model.BookReservation
import com.example.apverse.model.Computers
import com.example.apverse.model.RoomBooking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SHomeViewModel : ViewModel() {

    var computerList: ArrayList<Computers> = ArrayList()
    var bookingList: ArrayList<RoomBooking> = ArrayList()
    var reservationList: ArrayList<BookReservation> = ArrayList()

    suspend fun getComputerList() {
        computerList.clear()

        val data = Firestore().getFreeComputerList()
        for (i in data?.documents!!){
            val computer = i?.toObject(Computers::class.java)!!
            computerList.add(computer)
        }
    }

    suspend fun getBookingList() {
        bookingList.clear()

        val data = Firestore().getMyRoomBookingList()
        for (i in data?.documents!!){
            val booking = i?.toObject(RoomBooking::class.java)!!
            booking.doc_id = i.id

            val current = Calendar.getInstance().time
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
            val dataDate = booking?.date
            val today = dateFormatter.format(current)

            if(dataDate?.compareTo(today)!! >= 0){
                bookingList.add(booking)
            }
        }
    }

    suspend fun getReservationList() {
        reservationList.clear()

        val data = Firestore().getMyReservationList()
        for (i in data?.documents!!){
            val reservation = i?.toObject(BookReservation::class.java)!!
            reservation.doc_id = i.id
            reservationList.add(reservation)
        }
    }

    suspend fun cancelReservation(docId: String) : Boolean {
        return Firestore().deleteReservation(docId)
    }
}
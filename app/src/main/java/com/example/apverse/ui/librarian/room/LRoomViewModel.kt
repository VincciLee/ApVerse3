package com.example.apverse.ui.librarian.room

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.apverse.firestore.Firestore
import com.example.apverse.model.RoomBooking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class LRoomViewModel : ViewModel() {

    var roomBookingList: ArrayList<RoomBooking> = ArrayList()

    suspend fun getAllBookings() {
        roomBookingList.clear()

        val data = Firestore().getAllBookings()
        for (i in data?.documents!!){
            val booking = i?.toObject(RoomBooking::class.java)!!

            val current = Calendar.getInstance().time
            val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
            val dataDate = booking?.date
            val today = dateFormatter.format(current)

            if(dataDate?.compareTo(today) == 0){
                roomBookingList.add(booking)
            }
            else if(dataDate?.compareTo(today)!! > 0){
                roomBookingList.add(booking)
            }
        }
    }

}
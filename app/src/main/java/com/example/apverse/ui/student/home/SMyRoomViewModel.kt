package com.example.apverse.ui.student.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.RoomBooking
import com.example.apverse.model.Rooms
import com.example.apverse.utils.Constants
import kotlinx.coroutines.tasks.await

class SMyRoomViewModel: ViewModel() {

    var myRoomDetails: ArrayList<Rooms> = ArrayList()
    var myBookingInfo: RoomBooking = RoomBooking()
    var allBookingList: ArrayList<RoomBooking> = ArrayList()

    suspend fun getMyRoomBookingInfo(docId: String) {
//        val data =  FirestoreClass().mFireStore.collection(Constants.ROOM_BOOKING)
//                        .document(docId)
//                        .get()
//                        .await()
        val data = FirestoreClass().getMyRoomBookingInfo(docId)
        myBookingInfo = data?.toObject(RoomBooking::class.java)!!
        Log.i("ApVerse::SMyRoomVM", "getMyRoomBookingInfo() = Room "+myBookingInfo.room_id)
    }

    suspend fun getAllBookings() {
        val data = FirestoreClass().getAllBookings()
        for (i in data?.documents!!){
            val booking = i?.toObject(RoomBooking::class.java)!!
            allBookingList.add(booking)
        }
    }
}
package com.example.apverse.ui.student.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.apverse.firestore.Firestore
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.RoomBooking
import com.example.apverse.model.Rooms
import com.example.apverse.utils.Constants
import kotlinx.coroutines.tasks.await

class SMyRoomViewModel: ViewModel() {

    var myRoomDetails: ArrayList<Rooms> = ArrayList()
    var myBookingInfo: RoomBooking = RoomBooking()
    var allBookingList: ArrayList<RoomBooking> = ArrayList()

    suspend fun getMyRoomDetails(roomId: String) {
        val data = Firestore().getRoomDetails(roomId)
        for (i in data?.documents!!){
            val room = i?.toObject(Rooms::class.java)!!
            myRoomDetails.add(room)
        }
    }

    suspend fun getMyRoomBookingInfo(docId: String) {
        val data = Firestore().getMyRoomBookingInfo(docId)
        myBookingInfo = data?.toObject(RoomBooking::class.java)!!
        Log.i("ApVerse::SMyRoomVM", "getMyRoomBookingInfo() = Room "+myBookingInfo.room_id)
    }

    suspend fun getAllBookings() {
        val data = Firestore().getAllBookings()
        for (i in data?.documents!!){
            val booking = i?.toObject(RoomBooking::class.java)!!
            allBookingList.add(booking)
        }
    }

    suspend fun deleteMyBooking(docId: String) : Boolean{
        return Firestore().deleteMyBooking(docId)
    }

    suspend fun editMyBooking(docId: String, hashMap: HashMap<String, Any>) : Boolean{
        return Firestore().editMyBooking(docId, hashMap)
    }
}
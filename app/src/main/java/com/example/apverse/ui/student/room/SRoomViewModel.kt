package com.example.apverse.ui.student.room

import androidx.lifecycle.ViewModel
import com.example.apverse.firestore.Firestore
import com.example.apverse.model.Rooms

class SRoomViewModel : ViewModel() {

    var roomList: ArrayList<Rooms> = ArrayList()

    suspend fun getMyRoomDetails() {
        roomList.clear()

        val data = Firestore().getRoomList()
        for (i in data?.documents!!){
            val room = i?.toObject(Rooms::class.java)!!
            roomList.add(room)
        }
    }
}
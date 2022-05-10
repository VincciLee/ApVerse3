package com.example.apverse.firestore

import com.example.apverse.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class Firestore {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun getCurrentUserEmail():String{
        val User= FirebaseAuth.getInstance().currentUser

        var email = " "
        if (User != null){
            email = User.email.toString()
        }
        return email
    }

    suspend fun getRoomDetails(roomId: String) : QuerySnapshot?{

        return try {
            val data = mFireStore.collection(Constants.ROOMS)
                .whereEqualTo(Constants.ROOM_ID, roomId)
                .get()
                .await()
            data
        }
        catch (e: Exception) {
            null
        }
    }

    suspend fun getMyRoomBookingInfo(docId: String) : DocumentSnapshot?{

        return try {
            val data = mFireStore.collection(Constants.ROOM_BOOKING)
                .document(docId)
                .get()
                .await()
            data
        }
        catch (e: Exception) {
            null
        }
    }

    suspend fun getAllBookings() : QuerySnapshot?{

        return try {
            val data = mFireStore.collection(Constants.ROOM_BOOKING)
                .get()
                .await()
            data
        }
        catch (e: Exception) {
            null
        }
    }
}
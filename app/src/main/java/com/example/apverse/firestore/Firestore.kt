package com.example.apverse.firestore

import com.example.apverse.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

    suspend fun getMyRoomBookingList() : QuerySnapshot?{
        return try {
            val data = mFireStore.collection(Constants.ROOM_BOOKING)
                .whereEqualTo(Constants.STUDENT_EMAIL, getCurrentUserEmail())
                .orderBy(Constants.DATE)
                .orderBy(Constants.TIME)
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

    suspend fun editMyBooking(
        docId: String,
        hashMap: HashMap<String, Any>
    ) : Boolean {
        return try {
            val data = mFireStore.collection(Constants.ROOM_BOOKING)
                .document(docId)
                .update(hashMap)
                .await()
            true
        }
        catch (e: Exception) {
            false
        }
    }

    suspend fun deleteMyBooking(docId: String) : Boolean {
        return try {
            val data = mFireStore.collection(Constants.ROOM_BOOKING)
                .document(docId)
                .delete()
                .await()
            true
        }
        catch (e: Exception) {
            false
        }
    }

    suspend fun getMyReservationList() : QuerySnapshot?{
        return try {
            val data = mFireStore.collection(Constants.BOOK_RESERVATION)
                .whereEqualTo(Constants.STUDENT_EMAIL, getCurrentUserEmail())
                .orderBy(Constants.READY, Query.Direction.DESCENDING)
                .get()
                .await()
            data
        }
        catch (e: Exception) {
            null
        }
    }

    suspend fun getComputerList() : QuerySnapshot?{
        return try {
            val data = mFireStore.collection(Constants.COMPUTERS)
                .orderBy(Constants.COMPUTER_ID)
                .get()
                .await()
            data
        }
        catch (e: Exception) {
            null
        }
    }

    suspend fun getFreeComputerList() : QuerySnapshot?{
        return try {
            val data = mFireStore.collection(Constants.COMPUTERS)
                .whereEqualTo(Constants.COMPUTER_STATUS, Constants.FREE)
                .get()
                .await()
            data
        }
        catch (e: Exception) {
            null
        }
    }

    suspend fun updateComputerStatus(
        docId: String,
        hashMap: HashMap<String, Any>
    ) : Boolean {
        return try {
            val data = mFireStore.collection(Constants.COMPUTERS)
                .document(docId)
                .update(hashMap)
                .await()
            true
        }
        catch (e: Exception) {
            false
        }
    }
}
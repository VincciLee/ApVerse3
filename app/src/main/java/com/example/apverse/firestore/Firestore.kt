package com.example.apverse.firestore

import com.example.apverse.model.NewBookReservation
import com.example.apverse.model.NewRoomBooking
import com.example.apverse.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
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

    suspend fun getUserInfo() : QuerySnapshot?{
        return try {
            val data = mFireStore.collection(Constants.USERS)
                .whereEqualTo(Constants.USER_EMAIL, getCurrentUserEmail())
                .get()
                .await()
            data
        }
        catch (e: Exception) {
            null
        }
    }

    suspend fun getRoomList() : QuerySnapshot?{
        return try {
            val data = mFireStore.collection(Constants.ROOMS)
                .orderBy(Constants.ROOM_ID)
                .get()
                .await()
            data
        }
        catch (e: Exception) {
            null
        }
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

    suspend fun getSelectedRoomBookings(roomId: String, date: String) : QuerySnapshot?{
        return try {
            val data = mFireStore.collection(Constants.ROOM_BOOKING)
                .whereEqualTo(Constants.ROOM_ID, roomId)
                .whereEqualTo(Constants.DATE, date)
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

    suspend fun getBookingCount(date: String) : QuerySnapshot?{
        return try {
            val data = mFireStore.collection(Constants.ROOM_BOOKING)
                .whereEqualTo(Constants.DATE, date)
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

    suspend fun createRoomBooking(roomBooking: NewRoomBooking) : Boolean {
        return try {
            val data = mFireStore.collection(Constants.ROOM_BOOKING)
                .document()
                .set(roomBooking, SetOptions.merge())
                .await()
            true
        }
        catch (e: Exception) {
            false
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

    suspend fun getBookList(): QuerySnapshot?{
        return try {
            val data = mFireStore.collection(Constants.BOOKS)
                .orderBy(Constants.BOOK_TITLE)
                .get()
                .await()
            data
        }
        catch (e: Exception) {
            null
        }
    }

    suspend fun getBookDetails(bookId: String) : QuerySnapshot?{
        return try {
            val data = mFireStore.collection(Constants.BOOKS)
                .whereEqualTo(Constants.BOOK_ID, bookId)
                .get()
                .await()
            data
        }
        catch (e: Exception) {
            null
        }
    }

    suspend fun getBookReservation(bookId: String) : QuerySnapshot?{
        return try {
            val data = mFireStore.collection(Constants.BOOK_RESERVATION)
                .whereEqualTo(Constants.BOOK_ID, bookId)
                .orderBy(Constants.DATE)
                .get()
                .await()
            data
        }
        catch (e: Exception) {
            null
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

    suspend fun getBookReservationCount() : QuerySnapshot?{
        return try {
            val data = mFireStore.collection(Constants.BOOK_RESERVATION)
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

    suspend fun reserveBook(reservationInfo: NewBookReservation) : Boolean {
        return try {
            val data = mFireStore.collection(Constants.BOOK_RESERVATION)
                .document()
                .set(reservationInfo, SetOptions.merge())
                .await()
            true
        }
        catch (e: Exception) {
            false
        }
    }

    suspend fun deleteMyReservation(docId: String) : Boolean {
        return try {
            val data = mFireStore.collection(Constants.BOOK_RESERVATION)
                .document(docId)
                .delete()
                .await()
            true
        }
        catch (e: Exception) {
            false
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
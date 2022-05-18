package com.example.apverse.ui.librarian.book

import androidx.lifecycle.ViewModel
import com.example.apverse.firestore.Firestore
import com.example.apverse.model.BookReservation
import com.example.apverse.model.Books

class LBookReservationViewModel: ViewModel() {

    var reservationList: ArrayList<BookReservation> = ArrayList()
    var isFirst: Boolean = false
    var othersReady: Boolean = false
    var bookInfo: Books = Books()
    var errorMessage: String = ""

    suspend fun getAllReservations() {
        reservationList.clear()

        val data = Firestore().getAllReservations()
        for (i in data?.documents!!){
            val reservation = i?.toObject(BookReservation::class.java)!!
            reservation.doc_id = i.id
            reservationList.add(reservation)
        }
    }

    suspend fun validateReservation(docId: String, bookId: String, hashMap: HashMap<String, Any>) : Boolean {
        val data = Firestore().validateReservation(bookId)
        for (i in data?.documents!!){
            bookInfo = i.toObject(Books::class.java)!!
            bookInfo.doc_id = i.id
        }

        val data2 = Firestore().getBookReservation(bookId)
        var count = 0
        for (i in data2?.documents!!){
            count ++
            val reservation = i.toObject(BookReservation::class.java)!!
            reservation.doc_id = i.id

            if(reservation.doc_id == docId && count == 1){
                isFirst = true
            }

            if(reservation.ready == true) {
                othersReady = true
            }
        }

        if(bookInfo.book_status == "Borrowed") {
            errorMessage = "Cannot send alert because the book is being borrowed."
            return false
        }
        else if(othersReady) {
            errorMessage = "Cannot send alert because the book is ready to pick up by other students."
            return false
        }
        else if (!isFirst) {
            errorMessage = "Cannot send alert because other students booked this book first."
            return false
        }
        else {
            return updateReservation(docId, hashMap)
        }
    }

    suspend fun updateReservation(docId: String, hashMap: HashMap<String, Any>) : Boolean {
        val result = Firestore().updateReservation(docId, hashMap)

        if(!result) {
            errorMessage = "Unable to update the reservation."
        }

        return result
    }

    suspend fun deleteReservation(docId: String) : Boolean{
        return Firestore().deleteReservation(docId)
    }
}
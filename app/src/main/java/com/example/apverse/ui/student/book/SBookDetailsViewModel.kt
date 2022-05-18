package com.example.apverse.ui.student.book

import androidx.lifecycle.ViewModel
import com.example.apverse.firestore.Firestore
import com.example.apverse.model.BookReservation
import com.example.apverse.model.Books
import com.example.apverse.model.NewBookReservation
import com.example.apverse.model.Users

class SBookDetailsViewModel : ViewModel() {

    var userInfo: Users = Users()
    var bookInfo: Books = Books()
    var reservationList: ArrayList<BookReservation> = ArrayList()
    var hasReservation: Boolean = false
    var myReservationId: String = ""

    suspend fun getUserInfo() {
        val data = Firestore().getUserInfo()
        for (i in data?.documents!!){
            userInfo = i?.toObject(Users::class.java)!!
        }
    }

    suspend fun getBookDetails(bookId: String) {
        val data = Firestore().getBookDetails(bookId)
        for (i in data?.documents!!){
            bookInfo = i?.toObject(Books::class.java)!!
            bookInfo.doc_id = i.id
        }
    }

    suspend fun getBookReservation(bookId: String) {
        reservationList.clear()

        val data = Firestore().getBookReservation(bookId)
        for (i in data?.documents!!){
            val reservation = i?.toObject(BookReservation::class.java)!!
            reservation.doc_id = i.id
            reservationList.add(reservation)

            if(reservation.student_email == Firestore().getCurrentUserEmail()){
                hasReservation = true
                myReservationId = i.id
            }
        }
    }

    suspend fun reserveBook(reservationInfo: NewBookReservation): Boolean {
        return Firestore().reserveBook(reservationInfo)
    }

    suspend fun deleteMyReservation() : Boolean{
        return Firestore().deleteReservation(myReservationId)
    }
}
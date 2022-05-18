package com.example.apverse.ui.librarian.book

import androidx.lifecycle.ViewModel
import com.example.apverse.firestore.Firestore
import com.example.apverse.model.BookReservation
import com.example.apverse.model.Books
import com.example.apverse.utils.Constants

class LBookViewModel : ViewModel() {

    var bookList: ArrayList<Books> = ArrayList()
    var bookDetails: Books = Books()
    var reservationList: ArrayList<BookReservation> = ArrayList()

    suspend fun getBookList() {
        bookList.clear()

        val data = Firestore().getBookList()
        for (i in data?.documents!!){
            val book = i?.toObject(Books::class.java)!!
            book.doc_id = i.id
            bookList.add(book)
        }
    }

    suspend fun getBookDetails(bookId: String) {
        val data = Firestore().getBookDetails(bookId)
        for (i in data?.documents!!){
            bookDetails = i?.toObject(Books::class.java)!!
            bookDetails.doc_id = i.id
        }
    }

    suspend fun getReservationList(bookId: String) {
        reservationList.clear()

        val data = Firestore().getBookReservation(bookId)
        for (i in data?.documents!!){
            val reservation = i?.toObject(BookReservation::class.java)!!
            reservation.doc_id = i.id
            reservationList.add(reservation)
        }
    }

    suspend fun updateBookStatus() : Boolean {
        val bookHashMap: HashMap<String, Any> = HashMap()

        if(bookDetails.book_status == "Available"){
            bookHashMap[Constants.BOOK_STATUS] = "Borrowed"
        }
        else{
            bookHashMap[Constants.BOOK_STATUS] = "Available"
        }

        return Firestore().updateBookStatus(bookDetails.doc_id, bookHashMap)
    }
}
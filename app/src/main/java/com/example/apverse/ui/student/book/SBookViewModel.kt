package com.example.apverse.ui.student.book

import androidx.lifecycle.ViewModel
import com.example.apverse.firestore.Firestore
import com.example.apverse.model.Books

class SBookViewModel : ViewModel() {

    var bookList: ArrayList<Books> = ArrayList()

    suspend fun getBookList() {
        bookList.clear()

        val data = Firestore().getBookList()
        for (i in data?.documents!!){
            val book = i?.toObject(Books::class.java)!!
            book.doc_id = i.id
            bookList.add(book)
        }
    }
}
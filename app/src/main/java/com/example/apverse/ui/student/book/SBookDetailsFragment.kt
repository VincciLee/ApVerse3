package com.example.apverse.ui.student.book

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.adapter.BookDetailsAdapter
import com.example.apverse.databinding.FragmentSBookDetailsBinding
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.BookReservation
import com.example.apverse.model.Books
import com.example.apverse.model.NewBookReservation
import com.example.apverse.model.Users
import com.example.apverse.ui.BaseFragment
import com.google.firebase.firestore.auth.User
import com.google.type.DateTime
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class SBookDetailsFragment : BaseFragment() {

    private var _binding: FragmentSBookDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: SBookDetailsFragmentArgs by navArgs()

    private var bookTitle: String = ""
    private var bookImage: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSBookDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (requireActivity() as MainActivity).supportActionBar?.title = "Book Details"

        // Show book info
        getBookDetails(args.bookId)

        // Show reservation list
        loadReservation(args.bookId)

        // Reserve Book function
        binding.btnReserve.setOnClickListener{
            FirestoreClass().getUserDetails(this)
        }

        return root
    }

    private fun getBookDetails(bookId: String){
        showProgressDialog()
        FirestoreClass().getBookDetails(this, bookId)
    }

    fun showBookDetails(book: Books){
        hideProgressDialog()
        binding.textSBookDetailsTitle.text = book.book_title
        binding.textSBookDetailsId.text = book.book_id
        binding.textSBookDetailsAuthor.text = book.book_author
        binding.textSBookDetailsYear.text = book.book_year

        bookTitle = book.book_title
        bookImage = book.book_image

        Glide.with(this)
            .load(bookImage)
            .apply(
                RequestOptions.placeholderOf(R.drawable.unknown)
                .override(80,80))
            .into(binding.sBookDetailsImage)
    }

    fun failedGetBookDetails() {
        hideProgressDialog()
        showErrorSnackBar("Unable to get the book details.", true)
    }

    fun loadReservation(bookId: String){
//        showProgressDialog()
        FirestoreClass().getBookReservation(this, bookId)
    }

    fun successLoadReservation(myReservations: ArrayList<BookReservation>, hasReservation: Int) {
//        hideProgressDialog()

        if(hasReservation > 0) {
            binding.btnReserve.text = "Reserved"
            binding.btnReserve.isEnabled = false
        }

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.rv_s_book_details)
        recyclerView.adapter = BookDetailsAdapter(this, myReservations)
        recyclerView.setHasFixedSize(true)
    }

    fun reserveBook(myUser: Users){
        val bookId = args.bookId
        val status = "Reserved"
        val email = myUser.user_email
        val name = myUser.user_name

//        val c = Calendar.getInstance()
//        val year = c.get(Calendar.YEAR)
//        val month = c.get(Calendar.MONTH)
//        val day = c.get(Calendar.DAY_OF_MONTH)
//        val hour = c.get(Calendar.HOUR_OF_DAY)
//        val minute = c.get(Calendar.MINUTE)

//        val date = "$year-$month-$day"
//        val time = "$hour:$minute"

        val current = Calendar.getInstance().time
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        val timeFormatter = SimpleDateFormat("HH:mm")

        val date = dateFormatter.format(current)
        val time = timeFormatter.format(current)

        val reservation = NewBookReservation(
            bookId,
            bookImage,
            bookTitle,
            date,
            false,
            status,
            email,
            name,
            time
        )

        FirestoreClass().reserveBook(this, reservation)
    }

    fun successReserveBook(){
        showErrorSnackBar("Book Reserved", false)
    }

}
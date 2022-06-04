package com.example.apverse.ui.student.book

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.adapter.BookDetailsAdapter
import com.example.apverse.databinding.FragmentSBookDetailsBinding
import com.example.apverse.model.BookReservation
import com.example.apverse.model.Books
import com.example.apverse.model.NewBookReservation
import com.example.apverse.model.Users
import com.example.apverse.ui.BaseFragment
import com.example.apverse.utils.GlideLoader
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SBookDetailsFragment : BaseFragment() {

    private var _binding: FragmentSBookDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: SBookDetailsFragmentArgs by navArgs()
    private lateinit var viewModel: SBookDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(this).get(SBookDetailsViewModel::class.java)

        _binding = FragmentSBookDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (requireActivity() as MainActivity).supportActionBar?.title = "Book Details"

        viewLifecycleOwner.lifecycleScope.launch {
            // Show book info
            getBookDetails()

            // Show reservation list
            loadReservation()

            // Get user info
            viewModel.getUserInfo()
        }

        // Reserve Book function
        binding.btnReserve.setOnClickListener{
            val userInfo = viewModel.userInfo
            val bookInfo = viewModel.bookInfo
            val hasReservation = viewModel.hasReservation

            if(hasReservation == false) {
                // Reserve book
                viewLifecycleOwner.lifecycleScope.launch {
                    reserveBook(userInfo, bookInfo)
                }
            }
            else{
                // Cancel book reservation
                val builder = AlertDialog.Builder(this.context)
                builder.setMessage("Are you sure you want to cancel the book reservation?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        viewLifecycleOwner.lifecycleScope.launch {
                            deleteMyReservation()
                        }
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
        }

        return root
    }

    private suspend fun getBookDetails(){
        showProgressDialog()

        viewModel.getBookDetails(args.bookId)
        val bookInfo = viewModel.bookInfo
        showBookDetails(bookInfo)
    }

    fun showBookDetails(book: Books){
        hideProgressDialog()
        binding.textSBookDetailsTitle.text = book.book_title
        binding.textSBookDetailsId.text = book.book_id
        binding.textSBookDetailsAuthor.text = book.book_author
        binding.textSBookDetailsYear.text = book.book_year

        GlideLoader(this).loadImage(book.book_image, binding.sBookDetailsImage, 80)
    }

    suspend fun loadReservation(){
        viewModel.getBookReservation(args.bookId)
        val reservationList = viewModel.reservationList
        val hasReservation = viewModel.hasReservation

        successLoadReservation(reservationList, hasReservation)
    }

    fun successLoadReservation(myReservations: ArrayList<BookReservation>, hasReservation: Boolean) {
        if(hasReservation == true) {
            binding.btnReserve.text = "Cancel"
            binding.btnReserve.backgroundTintList = (ContextCompat.getColorStateList((requireActivity() as MainActivity), R.color.dark_red))
        }

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.rv_s_book_details)
        recyclerView.adapter = BookDetailsAdapter(this, myReservations)
        recyclerView.setHasFixedSize(true)
    }

    suspend fun reserveBook(myUser: Users, myBook: Books){
        val bookId = args.bookId
        val status = "Reserved"
        val email = myUser.user_email
        val name = myUser.user_name

        val current = Calendar.getInstance().time
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        val timeFormatter = SimpleDateFormat("HH:mm")

        val date = dateFormatter.format(current)
        val time = timeFormatter.format(current)

        val reservation = NewBookReservation(
            bookId,
            myBook.book_image,
            myBook.book_title,
            date,
            false,
            status,
            email,
            name,
            time
        )

        val result = viewModel.reserveBook(reservation)

        if(result) {
            successReserveBook()
        }
        else {
            failedReserveBook()
        }
    }

    fun successReserveBook(){
        showErrorSnackBar("Book Reserved", false)
        reload()
    }

    fun failedReserveBook(){
        showErrorSnackBar("Book Reservation failed.", true)
    }

    suspend fun deleteMyReservation() {
        val result = viewModel.deleteMyReservation()

        if(result) {
            showErrorSnackBar("Book reservation cancelled.", false)
            reload()
        }
        else {
            showErrorSnackBar("Book reservation cancellation failed..", true)
        }
    }

    private fun reload() {
        this.findNavController().navigate(SBookDetailsFragmentDirections.actionSBookDetailsFragmentSelf(args.bookId))
    }
}
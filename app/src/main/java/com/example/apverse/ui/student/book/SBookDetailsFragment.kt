package com.example.apverse.ui.student.book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.apverse.R
import com.example.apverse.adapter.BookDetailsAdapter
import com.example.apverse.databinding.FragmentSBookDetailsBinding
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.BookReservation
import com.example.apverse.model.Books
import com.example.apverse.ui.BaseFragment

class SBookDetailsFragment : BaseFragment() {

    private var _binding: FragmentSBookDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: SBookDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSBookDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Show book info
        getBookDetails(args.bookId)

        // Show reservation list
        loadReservation(args.bookId)

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

        Glide.with(this)
            .load(book.book_image)
            .apply(
                RequestOptions.placeholderOf(R.drawable.unknown)
                .override(80,80))
            .into(binding.sBookDetailsImage)
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
}
package com.example.apverse.ui.librarian.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.adapter.BookDetailsAdapter
import com.example.apverse.databinding.FragmentLBookDetailsBinding
import com.example.apverse.model.BookReservation
import com.example.apverse.model.Books
import com.example.apverse.ui.BaseFragment
import com.example.apverse.utils.GlideLoader
import kotlinx.coroutines.launch

class LBookDetailsFragment : BaseFragment() {

    private var _binding: FragmentLBookDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: LBookDetailsFragmentArgs by navArgs()

    private var docId: String = ""

    private lateinit var viewModel: LBookViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LBookViewModel::class.java)

        _binding = FragmentLBookDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (requireActivity() as MainActivity).supportActionBar?.title = "Book Details"

        viewLifecycleOwner.lifecycleScope.launch {
            // Show book info
            getBookDetails()

            // Show reservation list
            loadReservation()
        }

        binding.btnBorrow.setOnClickListener{
            viewLifecycleOwner.lifecycleScope.launch {
                updateBookStatus()
            }
        }

        return root
    }

    private suspend fun getBookDetails(){
        showProgressDialog()

        viewModel.getBookDetails(args.bookId)
        val bookInfo = viewModel.bookDetails
        showBookDetails(bookInfo)
    }

    fun showBookDetails(book: Books){
        hideProgressDialog()
        binding.textLBookDetailsTitle.text = book.book_title
        binding.textLBookDetailsId.text = book.book_id
        binding.textLBookDetailsAuthor.text = book.book_author
        binding.textLBookDetailsYear.text = book.book_year

        docId = book.doc_id


        GlideLoader(this).loadImage(book.book_image, binding.lBookDetailsImage, 80)

        if(book.book_status == "Available"){
            binding.btnBorrow.text = "Borrow"
        }
        else{
            binding.btnBorrow.text = "Return"
        }

    }

    suspend fun loadReservation(){
        viewModel.getReservationList(args.bookId)
        val reservationList = viewModel.reservationList
        successLoadReservation(reservationList)
    }

    fun successLoadReservation(myReservations: ArrayList<BookReservation>) {
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.rv_l_book_details)
        recyclerView.adapter = BookDetailsAdapter(this, myReservations)
        recyclerView.setHasFixedSize(true)
    }

    private suspend fun updateBookStatus() {
        val result = viewModel.updateBookStatus()

        if(result) {
            successUpdateBookStatus()
        }
        else {
            failedUpdateBookStatus()
        }
    }

    fun successUpdateBookStatus() {
        showErrorSnackBar("Book Status updated.", false)
        reload()
    }

    fun failedUpdateBookStatus() {
        showErrorSnackBar("Book Status update failed..", true)
    }

    private fun reload() {
        this.findNavController().navigate(LBookDetailsFragmentDirections.actionLBookDetailsFragmentSelf(args.bookId))
    }
}
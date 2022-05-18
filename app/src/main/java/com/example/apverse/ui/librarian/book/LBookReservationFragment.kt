package com.example.apverse.ui.librarian.book

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.adapter.BookReservationAdapter
import com.example.apverse.databinding.FragmentLBookReservationBinding
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.BookReservation
import com.example.apverse.ui.BaseFragment
import kotlinx.coroutines.launch

class LBookReservationFragment : BaseFragment() {

    private var _binding: FragmentLBookReservationBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LBookReservationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LBookReservationViewModel::class.java)

        _binding = FragmentLBookReservationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_l_book_reservation, null)

        (requireActivity() as MainActivity).supportActionBar?.title = "Book Reservation"

        viewLifecycleOwner.lifecycleScope.launch {
            loadReservations()
        }

        return root
    }

    suspend fun loadReservations() {
        showProgressDialog()
//        FirestoreClass().getReservations(this)
        viewModel.getAllReservations()
        val reservationList = viewModel.reservationList
        successLoadReservation(reservationList)
    }

    fun successLoadReservation(myReservation: ArrayList<BookReservation>) {
        hideProgressDialog()
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.rv_l_reservation_list)
        recyclerView.adapter = BookReservationAdapter(this, myReservation)
        recyclerView.setHasFixedSize(true)
    }

    suspend fun validateReservation(docId: String, bookId: String, hashMap: HashMap<String, Any>) {
        val result = viewModel.validateReservation(docId, bookId, hashMap)

        if(result) {
            successUpdateReservation()
        }
        else {
            val message = viewModel.errorMessage
            failedUpdateReservation(message)
        }
    }

    fun successUpdateReservation(){
        Log.i("ApVerse::LFragment", "LBookReservationFragment - successUpdateReservation()")
        showErrorSnackBar("Book has ready and alert sent to the student.", false)
        reload()
    }

    fun failedUpdateReservation(message: String){
        showErrorSnackBar("$message", true)
    }

    suspend fun deleteReservation(docId: String) {
        val result = viewModel.deleteReservation(docId)

        if(result) {
            successDeleteReservation()
        }
        else {
            failedDeleteReservation()
        }
    }

    fun successDeleteReservation(){
        Log.i("ApVerse::LFragment", "LBookReservationFragment - successDeleteReservation()")
        showErrorSnackBar("Book reservation has been removed.", false)
        reload()
    }

    fun failedDeleteReservation(){
        showErrorSnackBar("Book reservation removal failed.", false)
    }

    fun reload() {
        this.findNavController().navigate(R.id.action_nav_l_reservation_self)
    }
}
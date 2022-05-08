package com.example.apverse.ui.librarian.book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.adapter.BookReservationAdapter
import com.example.apverse.databinding.FragmentLBookReservationBinding
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.BookReservation
import com.example.apverse.ui.BaseFragment

class LBookReservationFragment : BaseFragment() {

    private var _binding: FragmentLBookReservationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLBookReservationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_l_book_reservation, null)

        (requireActivity() as MainActivity).supportActionBar?.title = "Book Reservation"

        loadReservations()

        return root
    }

    fun loadReservations() {
        showProgressDialog()
        FirestoreClass().getReservations(this)
    }

    fun successLoadReservation(myReservation: ArrayList<BookReservation>) {
        hideProgressDialog()
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.rv_l_reservation_list)
        recyclerView.adapter = BookReservationAdapter(this, myReservation)
        recyclerView.setHasFixedSize(true)
    }
}
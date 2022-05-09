package com.example.apverse.ui.student.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.adapter.MyBookingAdapter
import com.example.apverse.adapter.MyReservationAdapter
import com.example.apverse.databinding.FragmentSHomeBinding
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.BookReservation
import com.example.apverse.model.Computers
import com.example.apverse.model.RoomBooking
import com.example.apverse.ui.BaseFragment
import com.google.android.material.navigation.NavigationView

class SHomeFragment : BaseFragment() {

    private var _binding: FragmentSHomeBinding? = null
    private val binding get() = _binding!!

//    companion object {
//        fun newInstance() = SHomeFragment()
//    }
//
//    private lateinit var viewModel: SHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val sHomeViewModel =
//            ViewModelProvider(this).get(SHomeViewModel::class.java)

        _binding = FragmentSHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_s_home, null)

        (requireActivity() as MainActivity).supportActionBar?.title = "Homepage"

        getComputerCount()

        getMyBookings()

        getMyBookReservations()

        return root
    }

    private fun getComputerCount() {
        FirestoreClass().getComputerCount(this)
    }

    fun successGetComputerCount(computerList: ArrayList<Computers>) {
        binding.textSComputerCount.text = computerList.size.toString()
    }

    private fun getMyBookings() {
        FirestoreClass().getMyBookings(this)
    }

    fun successGetMyBookings(myBookingList: ArrayList<RoomBooking>) {
        val recyclerView = binding.rvSHomeBook
        recyclerView.adapter = MyBookingAdapter(this, myBookingList)
        recyclerView.setHasFixedSize(true)
    }

    fun failedGetMyBookings() {
        showErrorSnackBar("Unable to retrieve your room booking records.", true)
    }

    private fun getMyBookReservations() {
        FirestoreClass().getMyBookReservations(this)
    }

    fun successGetMyReservations(myReservationList: ArrayList<BookReservation>) {
        val recyclerView = binding.rvSHomeReservation
        recyclerView.adapter = MyReservationAdapter(this, myReservationList)
        recyclerView.setHasFixedSize(true)
    }

    fun failedGetMyReservations() {
        showErrorSnackBar("Unable to retrieve your book reservation records.", true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(SHomeViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}
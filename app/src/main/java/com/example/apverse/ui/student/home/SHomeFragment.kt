package com.example.apverse.ui.student.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
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
import kotlinx.coroutines.launch

class SHomeFragment : BaseFragment() {

    private var _binding: FragmentSHomeBinding? = null
    private val binding get() = _binding!!

//    companion object {
//        fun newInstance() = SHomeFragment()
//    }
//
    private lateinit var viewModel: SHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SHomeViewModel::class.java)

        _binding = FragmentSHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_s_home, null)

        (requireActivity() as MainActivity).supportActionBar?.title = "Homepage"

        viewLifecycleOwner.lifecycleScope.launch {
            getComputerCount()
            getMyBookings()
            getMyBookReservations()
        }

        return root
    }

    private suspend fun getComputerCount() {
        Log.i("ApVerse::SHomeFragment", "getComputerCount()")
//        FirestoreClass().getComputerCount(this)
        viewModel.getComputerList()

        Log.i("ApVerse::SHomeFragment", "getComputerCount() - getting computerList from vm")
        val computerList = viewModel.computerList

        successGetComputerCount(computerList)
    }

    fun successGetComputerCount(computerList: ArrayList<Computers>) {
        Log.i("ApVerse::SHomeFragment", "successGetComputerCount()")
        binding.textSComputerCount.text = computerList.size.toString()
    }

    private suspend fun getMyBookings() {
        Log.i("ApVerse::SHomeFragment", "getMyBookings()")
//        FirestoreClass().getMyBookings(this)
        viewModel.getBookingList()
        val bookingList = viewModel.bookingList

        successGetMyBookings(bookingList)
    }

    fun successGetMyBookings(myBookingList: ArrayList<RoomBooking>) {
        Log.i("ApVerse::SHomeFragment", "successGetMyBookings()")
        val recyclerView = binding.rvSHomeBook
        recyclerView.adapter = MyBookingAdapter(this, myBookingList)
        recyclerView.setHasFixedSize(true)
    }

    fun failedGetMyBookings() {
        showErrorSnackBar("Unable to retrieve your room booking records.", true)
    }

    private suspend fun getMyBookReservations() {
        Log.i("ApVerse::SHomeFragment", "getMyBookReservations()")
//        FirestoreClass().getMyBookReservations(this)
        viewModel.getReservationList()
        val reservationList = viewModel.reservationList

        successGetMyReservations(reservationList)
    }

    fun successGetMyReservations(myReservationList: ArrayList<BookReservation>) {
        Log.i("ApVerse::SHomeFragment", "successGetMyReservations()")
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
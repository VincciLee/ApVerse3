package com.example.apverse.ui.student.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.adapter.MyBookingAdapter
import com.example.apverse.adapter.MyReservationAdapter
import com.example.apverse.databinding.FragmentSHomeBinding
import com.example.apverse.model.BookReservation
import com.example.apverse.model.Computers
import com.example.apverse.model.RoomBooking
import com.example.apverse.ui.BaseFragment
import kotlinx.coroutines.launch

class SHomeFragment : BaseFragment() {

    private lateinit var viewModel: SHomeViewModel

    private var _binding: FragmentSHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SHomeViewModel::class.java)

        _binding = FragmentSHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (requireActivity() as MainActivity).supportActionBar?.title = "Homepage"

        viewLifecycleOwner.lifecycleScope.launch {
            getComputerCount()
            getMyBookings()
            getMyBookReservations()
        }

        return root
    }

    private suspend fun getComputerCount() {
        viewModel.getComputerList()
        val computerList = viewModel.computerList

        successGetComputerCount(computerList)
    }

    fun successGetComputerCount(computerList: ArrayList<Computers>) {
        binding.textSComputerCount.text = computerList.size.toString()
    }

    private suspend fun getMyBookings() {
        viewModel.getBookingList()
        val bookingList = viewModel.bookingList

        successGetMyBookings(bookingList)
    }

    fun successGetMyBookings(myBookingList: ArrayList<RoomBooking>) {
        val recyclerView = binding.rvSHomeBook
        recyclerView.adapter = MyBookingAdapter(this, myBookingList)
        recyclerView.setHasFixedSize(true)
    }

    private suspend fun getMyBookReservations() {
        viewModel.getReservationList()
        val reservationList = viewModel.reservationList

        successGetMyReservations(reservationList)
    }

    fun successGetMyReservations(myReservationList: ArrayList<BookReservation>) {
        val recyclerView = binding.rvSHomeReservation
        recyclerView.adapter = MyReservationAdapter(this, myReservationList)
        recyclerView.setHasFixedSize(true)
    }

    suspend fun cancelReservation(docId: String) {
        val result = viewModel.cancelReservation(docId)

        if (result) {
            successCancelReservations()
        }
        else {
            failedCancelReservations()
        }
    }

    fun successCancelReservations() {
        showErrorSnackBar("Book reservation cancelled.", false)
        reload()
    }

    fun failedCancelReservations() {
        showErrorSnackBar("Unable to cancel book reservation.", true)
    }

    fun reload() {
        this.findNavController().navigate(SHomeFragmentDirections.actionNavSHomeSelf())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package com.example.apverse.ui.librarian.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.databinding.FragmentLHomeBinding
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.BookReservation
import com.example.apverse.model.RoomBooking
import com.example.apverse.ui.BaseFragment
import java.text.SimpleDateFormat
import java.util.*

class LHomeFragment : BaseFragment() {

    private var _binding: FragmentLHomeBinding? = null
    private val binding get() = _binding!!

//    companion object {
//        fun newInstance() = LHomeFragment()
//    }
//
//    private lateinit var viewModel: LHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val lHomeViewModel = ViewModelProvider(this).get(LHomeViewModel::class.java)

        _binding = FragmentLHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_l_home, null)

        (requireActivity() as MainActivity).supportActionBar?.title = "Homepage"

//        binding.textLHomeBook.text = viewModel.bookingCount.toString()

//        val textView: TextView = binding.textLHome
//        lHomeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        getBookingCount()
        getReservationCount()

        return root
    }

    private fun getBookingCount() {
//        showProgressDialog()

        val current = Calendar.getInstance().time
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        val today = dateFormatter.format(current)

        FirestoreClass().getBookingCount(this, today)
    }

    fun successGetBookingCount(bookingList: ArrayList<RoomBooking>) {
        binding.textLHomeBook.text = bookingList.size.toString()
//        hideProgressDialog()
    }

    private fun getReservationCount() {
        FirestoreClass().getReservations(this)
    }

    fun successGetReservationCount(reservationList: ArrayList<BookReservation>) {
        binding.textLHomeReservation.text = reservationList.size.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(LHomeViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}
package com.example.apverse.ui.librarian.room

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.adapter.RoomBookingAdapter
import com.example.apverse.databinding.FragmentLRoomBinding
import com.example.apverse.model.RoomBooking
import com.example.apverse.ui.BaseFragment
import kotlinx.coroutines.launch

class LRoomFragment : BaseFragment() {

    private lateinit var viewModel: LRoomViewModel

    private var _binding: FragmentLRoomBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LRoomViewModel::class.java)

        _binding = FragmentLRoomBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (requireActivity() as MainActivity).supportActionBar?.title = "Discussion Room"

        viewLifecycleOwner.lifecycleScope.launch {
            loadRoomBookings()
        }

        binding.swtBooking?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewLifecycleOwner.lifecycleScope.launch {
                    binding.swtBooking.text = "Upcoming"
                    binding.textLBookingTitle.text = "Booking List"
                    binding.btnLClearBooking.visibility = View.INVISIBLE
                    loadRoomBookings()
                }
            } else {
                viewLifecycleOwner.lifecycleScope.launch {
                    binding.swtBooking.text = "History"
                    binding.textLBookingTitle.text = "Booking History"
                    binding.btnLClearBooking.visibility = View.VISIBLE
                    loadBookingHistory()
                }
            }
        }

        binding.btnLClearBooking.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                clearBookingHistory()
            }
        }

        return root
    }

    private suspend fun loadRoomBookings() {
        showProgressDialog()

        viewModel.getAllBookings()
        val bookingList = viewModel.roomBookingList
        successLoadRoomBoookings(bookingList)
    }

    private suspend fun loadBookingHistory() {
        showProgressDialog()
        viewModel.getBookingHistory()
        val bookingList = viewModel.bookingHistoryList
        successLoadRoomBoookings(bookingList)
    }

    fun successLoadRoomBoookings(myBookings: ArrayList<RoomBooking>) {
        hideProgressDialog()
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.rv_l_room)
        recyclerView.adapter = RoomBookingAdapter(this, myBookings)
        recyclerView.setHasFixedSize(true)
    }

    suspend fun clearBookingHistory() {
        val result = viewModel.clearBookingHistory()

        if(result) {
            successClearBoookings()
        }
        else {
            failedClearBoookings()
        }
    }

    fun successClearBoookings() {
        showErrorSnackBar("Clear room booking successfully.", false)
        reload()
    }

    fun failedClearBoookings() {
        showErrorSnackBar("Failed to clear the bookings.", true)
    }

    fun reload() {
        this.findNavController().navigate(LRoomFragmentDirections.actionNavLRoomSelf())
    }
}
package com.example.apverse.ui.librarian.room

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.adapter.RoomBookingAdapter
import com.example.apverse.databinding.FragmentLBookBinding
import com.example.apverse.databinding.FragmentLRoomBinding
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.RoomBooking
import com.example.apverse.ui.BaseFragment
import com.example.apverse.utils.Constants

class LRoomFragment : BaseFragment() {

//    companion object {
//        fun newInstance() = LRoomFragment()
//    }
//
//    private lateinit var viewModel: LRoomViewModel

    private var _binding: FragmentLRoomBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLRoomBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (requireActivity() as MainActivity).supportActionBar?.title = "Discussion Room"

        loadRoomBookings()

        return root
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(LRoomViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

    private fun loadRoomBookings() {
        showProgressDialog()
        FirestoreClass().getRoomBookings(this)
    }

    fun successLoadRoomBoookings(myBookings: ArrayList<RoomBooking>) {
        Log.i("ApVerse::LRoomFragment", "successLoadRoomBoookings()")
        hideProgressDialog()
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.rv_l_room)
        recyclerView.adapter = RoomBookingAdapter(this, myBookings)
        recyclerView.setHasFixedSize(true)
    }

    fun failedLoadRoomBoookings(message: String) {
        hideProgressDialog()
        showErrorSnackBar(message, true)
    }
}
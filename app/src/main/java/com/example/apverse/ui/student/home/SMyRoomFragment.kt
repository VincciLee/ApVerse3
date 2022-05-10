package com.example.apverse.ui.student.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.navigation.fragment.navArgs
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.databinding.FragmentSMyRoomBinding
import kotlinx.coroutines.launch

class SMyRoomFragment : Fragment() {

    private var _binding: FragmentSMyRoomBinding? = null
    private val binding get() = _binding!!
    private val args: SMyRoomFragmentArgs by navArgs()
    private lateinit var viewModel: SMyRoomViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SMyRoomViewModel::class.java)

        _binding = FragmentSMyRoomBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_s_my_room, null)

        (requireActivity() as MainActivity).supportActionBar?.title = "Room Booking"

//        scope.launch
//        getMyRoomBookingInfo()
        viewLifecycleOwner.lifecycleScope.launch {
            whenResumed {
//                showMyRoomBookingInfo()
                showAllBookings()
            }
        }

        return root
    }

    suspend fun showMyRoomBookingInfo() {
        viewModel.getMyRoomBookingInfo(args.docId)
        val myBookingInfo = viewModel.myBookingInfo

        val roomId = myBookingInfo.room_id
        val name = myBookingInfo.student_name
        val date = myBookingInfo.date
        val time = myBookingInfo.time

        binding.textSHomeTest.text = "Room $roomId, $name, $date, $time"
    }

    suspend fun showAllBookings() {
        viewModel.getAllBookings()
        val allBookings = viewModel.allBookingList
        var text = ""

        for(i in allBookings) {
            text = text + "Room " + i.room_id + ", " + i.student_name + ", " + i.date + "\n"
        }

        binding.textSHomeTest.text = text
    }
}
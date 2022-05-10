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
import com.example.apverse.ui.DatePickerFragment
import com.example.apverse.ui.TimePickerFragment
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

        viewLifecycleOwner.lifecycleScope.launch {
            whenResumed {
                showMyRoomsDetails()
                showMyRoomBookingInfo()
//                showAllBookings()
            }
        }

        binding.btnSMyDate.setOnClickListener {
            val date = binding.inputSMyDate.editText?.text.toString().trim { it <= ' ' }
            setDate(date)
        }

        binding.btnSMyTime.setOnClickListener {
            val time = binding.inputSMyTime.editText?.text.toString().trim { it <= ' ' }
            setTime(time)
        }

        return root
    }

    suspend fun showMyRoomsDetails() {
        viewModel.getMyRoomDetails(args.roomId)
        val myRoomDetails = viewModel.myRoomDetails

        val roomId = myRoomDetails[0].room_id
        val capacity = myRoomDetails[0].capacity

        binding.textSMyRoomId.text = "Discussion Room : $roomId"
        binding.textSMyCapacity.text = "Capacity : $capacity"
    }

    suspend fun showMyRoomBookingInfo() {
        viewModel.getMyRoomBookingInfo(args.docId)
        val myBookingInfo = viewModel.myBookingInfo

        val name = myBookingInfo.student_name
        val tp = myBookingInfo.student_tp
        val date = myBookingInfo.date
        val time = myBookingInfo.time

        binding.inputSMyName.editText?.setText(name)
        binding.inputSMyTp.editText?.setText(tp)
        binding.inputSMyDate.editText?.setText(date)
        binding.inputSMyTime.editText?.setText(time)

        setDate(date)
        setTime(time)
    }

    private fun setDate(date: String) {
        val datePickerFragment = DatePickerFragment(date)
        val supportFragmentManager = requireActivity().supportFragmentManager

        supportFragmentManager.setFragmentResultListener(
            "REQUEST_KEY",
            viewLifecycleOwner
        ){
                resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
            val date = bundle.getString("SELECTED_DATE")

            binding.inputSMyDate.editText?.setText(date)
        }
        }

        datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
    }

    private fun setTime(time: String) {
        val timePickerFragment = TimePickerFragment(time)
        val supportFragmentManager = requireActivity().supportFragmentManager

        supportFragmentManager.setFragmentResultListener(
            "REQUEST_KEY",
            viewLifecycleOwner
        ){
                resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
            val time = bundle.getString("SELECTED_TIME")

            binding.inputSMyTime.editText?.setText(time)
        }
        }

        timePickerFragment.show(supportFragmentManager, "TimePickerFragment")
    }

    suspend fun showAllBookings() {
        viewModel.getAllBookings()
        val allBookings = viewModel.allBookingList
        var text = ""

        for(i in allBookings) {
            text = text + "Room " + i.room_id + ", " + i.student_name + ", " + i.date + "\n"
        }

//        binding.textSHomeTest.text = text
    }
}
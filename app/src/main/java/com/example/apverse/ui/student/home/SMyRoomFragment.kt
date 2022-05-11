package com.example.apverse.ui.student.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.lifecycle.whenResumed
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.databinding.FragmentSMyRoomBinding
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.NewRoomBooking
import com.example.apverse.ui.BaseFragment
import com.example.apverse.ui.DatePickerFragment
import com.example.apverse.ui.TimePickerFragment
import com.example.apverse.utils.Constants
import kotlinx.coroutines.launch

class SMyRoomFragment : BaseFragment() {

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
            showMyRoomsDetails()
            showMyRoomBookingInfo()
//            whenResumed {
//                showAllBookings()
//            }
        }

//        binding.btnSMyDate.iconTint = Icons.Filled.CalendarToday
//        binding.btnSMyDate.icon = Icons.Filled.CalendarToday

        binding.btnSMyDate.setOnClickListener {
            val date = binding.inputSMyDate.editText?.text.toString().trim { it <= ' ' }
            setDate(date)
        }

        binding.btnSMyTime.setOnClickListener {
            val time = binding.inputSMyTime.editText?.text.toString().trim { it <= ' ' }
            setTime(time)
        }

        binding.btnSEditBooking.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                editMyBooking()
            }
        }

        binding.btnSDeleteBooking.setOnClickListener {
            val builder = AlertDialog.Builder(this.context)
            builder.setMessage("Are you sure you want to cancel the room booking?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    viewLifecycleOwner.lifecycleScope.launch {
                        deleteMyBooking()
                    }
                }
                .setNegativeButton("No") { dialog, id ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
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
        val hasHdmi = myBookingInfo.hdmi_cable

        binding.inputSMyName.editText?.setText(name)
        binding.inputSMyTp.editText?.setText(tp)
        binding.inputSMyDate.editText?.setText(date)
        binding.inputSMyTime.editText?.setText(time)
        binding.checkedSMyHdmi.isChecked = hasHdmi
    }

    suspend fun editMyBooking() {
        val name = binding.inputSMyName.editText?.text.toString().trim { it <= ' ' }
        val tp = binding.inputSMyTp.editText?.text.toString().trim { it <= ' ' }
        val date = binding.inputSMyDate.editText?.text.toString().trim { it <= ' ' }
        val time = binding.inputSMyTime.editText?.text.toString().trim { it <= ' ' }
        val hasHdmi = binding.checkedSMyHdmi.isChecked

        val bookingHashMap: HashMap<String, Any> = HashMap()
        bookingHashMap[Constants.STUDENT_NAME] = name
        bookingHashMap[Constants.STUDENT_TP] = tp
        bookingHashMap[Constants.DATE] = date
        bookingHashMap[Constants.TIME] = time
        bookingHashMap[Constants.HDMI_CABLE] = hasHdmi

        val result = viewModel.editMyBooking(args.docId, bookingHashMap)

        if(result) {
            showErrorSnackBar("Room booking updated.", false)
            this.findNavController().navigate(SMyRoomFragmentDirections.actionSMyRoomFragmentToNavSHome())
        }
        else{
            showErrorSnackBar("Failed to update room booking.", true)
            this.findNavController().navigate(SMyRoomFragmentDirections.actionSMyRoomFragmentToNavSHome())
        }
    }

    suspend fun deleteMyBooking() {
        val result = viewModel.deleteMyBooking(args.docId)

        if(result) {
            showErrorSnackBar("Room booking cancelled.", false)
            this.findNavController().navigate(SMyRoomFragmentDirections.actionSMyRoomFragmentToNavSHome())
        }
        else{
            showErrorSnackBar("Failed to cancel room booking.", true)
            this.findNavController().navigate(SMyRoomFragmentDirections.actionSMyRoomFragmentToNavSHome())
        }
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

//    suspend fun showAllBookings() {
//        viewModel.getAllBookings()
//        val allBookings = viewModel.allBookingList
//        var text = ""
//
//        for(i in allBookings) {
//            text = text + "Room " + i.room_id + ", " + i.student_name + ", " + i.date + "\n"
//        }
//
////        binding.textSHomeTest.text = text
//    }
}
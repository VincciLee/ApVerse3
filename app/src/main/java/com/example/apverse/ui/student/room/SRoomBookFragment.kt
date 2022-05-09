package com.example.apverse.ui.student.room

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.apverse.R
import com.example.apverse.databinding.FragmentSRoomBookBinding
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.NewRoomBooking
import com.example.apverse.model.RoomBooking
import com.example.apverse.ui.BaseFragment
import com.example.apverse.ui.DatePickerFragment
import com.example.apverse.ui.TimePickerFragment
import java.util.*

class SRoomBookFragment : BaseFragment() {

    private var _binding: FragmentSRoomBookBinding? = null
    private val binding get() = _binding!!
    private val args: SRoomBookFragmentArgs by navArgs()

//    companion object {
//        private const val ROOM_ID = "room_id"
//        private const val CAPACITY = "capacity"
//
//        fun newInstance(myRoomId: String, myCapacity: String) = SRoomBookFragment().apply {
//            arguments = Bundle(2).apply {
//                putString(ROOM_ID, myRoomId)
//                putString(CAPACITY, myCapacity)
//            }
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSRoomBookBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.textRoomId.text = "Discussion Room : "+args.roomId
        binding.textCapacity.text = "Capacity : "+args.capacity

        binding.btnDate.setOnClickListener {
            val datePickerFragment = DatePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager

            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ){
                resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
                    val date = bundle.getString("SELECTED_DATE")

                    binding.inputDate.editText?.setText(date)
                }
            }

            datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
        }

        binding.btnTime.setOnClickListener {
            val timePickerFragment = TimePickerFragment()
            val supportFragmentManager = requireActivity().supportFragmentManager

            supportFragmentManager.setFragmentResultListener(
                "REQUEST_KEY",
                viewLifecycleOwner
            ){
                resultKey, bundle -> if (resultKey == "REQUEST_KEY") {
                    val time = bundle.getString("SELECTED_TIME")

                    binding.inputTime.editText?.setText(time)
                }
            }

            timePickerFragment.show(supportFragmentManager, "TimePickerFragment")
        }

        binding.btnBookRoom.setOnClickListener {

        }

        binding.btnBookRoom.setOnClickListener {
            saveBookingDetails()
        }

//        val c = Calendar.getInstance()
//        val year = c.get(Calendar.YEAR)
//        val month = c.get(Calendar.MONTH)
//        val day = c.get(Calendar.DAY_OF_MONTH)
//
//        binding.btnDate.setOnClickListener {
//            val datePickerDialog = DatePickerDialog.newInstance(DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
//
//                // do something here
//            }, year, month, day)
//
//            datePickerDialog.show()
//
//             Another method
//            val datePickerFragment = DatePickerFragment()
//            val supportFragmentManager = activity?.supportFragmentManager
//        }

        return root
    }

    private fun saveBookingDetails(): Boolean{
        val name = binding.inputStudentName.editText?.text.toString().trim { it <= ' ' }
        val tp = binding.inputStudentTp.editText?.text.toString().trim { it <= ' ' }
        val date = binding.inputDate.editText?.text.toString().trim { it <= ' ' }
        val time = binding.inputTime.editText?.text.toString().trim { it <= ' ' }

        val roomBooking = NewRoomBooking(
            args.roomId,
            FirestoreClass().getCurrentUserEmail(),
            name,
            tp,
            date,
            time,
            binding.checkedHdmi.isChecked
        )

        return when{
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please do not leave the name field blank", true)
                true
            }
            TextUtils.isEmpty(tp) -> {
                showErrorSnackBar("Please do not leave the tp field blank", true)
                true
            }
            TextUtils.isEmpty(date) -> {
                showErrorSnackBar("Please do not leave the date field blank", true)
                true
            }
            TextUtils.isEmpty(time) -> {
                showErrorSnackBar("Please do not leave the time field blank", true)
                true
            }
            else -> {
                FirestoreClass().validateRoomBooking(this, roomBooking)
                false
            }
        }
    }

    fun saveRoomBookingSuccess() {
        showErrorSnackBar("Discussion Room Booked", false)
        findNavController().navigate(R.id.action_SRoomBookFragment_to_nav_s_room)
        Log.i("ApVerse::SRoomBook", "Discussion Room Booked.")
    }

    fun saveRoomBookingFailure(message:String) {
        showErrorSnackBar(message, true)
    }
}
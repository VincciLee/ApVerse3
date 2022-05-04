package com.example.apverse.ui.student.room

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.adapter.RoomAdapter
import com.example.apverse.databinding.FragmentSRoomBinding
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.firestore.Room
import com.example.apverse.model.Rooms
import com.example.apverse.ui.BaseFragment


class SRoomFragment : BaseFragment() {

//    companion object {
//        fun newInstance() = SRoomFragment()
//    }
//
//    private lateinit var viewModel: SRoomViewModel

    private var _binding: FragmentSRoomBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Bind to Book Fragment
        _binding = FragmentSRoomBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inflate Room Booking xml
        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_s_room_book, null)

//        binding.button.setOnClickListener{ root ->
//            root.findNavController().navigate(R.id.action_nav_s_room_to_SRoomBookFragment)
//        }

        (requireActivity() as MainActivity).supportActionBar?.title = "Discussion Room"

//        // Load data into recycler view (Scrolling Screen)
        loadRooms()
////        val myRoomDataset = Room().loadRooms()
//        val myRoomDataset = FirestoreClass().getRooms()
//        Log.i("Apverse", "Room Fragment: "+myRoomDataset[0].room_id)
//
//        val recyclerView = root.findViewById<RecyclerView>(R.id.rv_room)
//        recyclerView.adapter = RoomAdapter(this, myRoomDataset)
//        recyclerView.setHasFixedSize(true)

        // Listen to Book button and pass data to the booking page
//        val book = view.findViewById<View>(R.id.btn_Book) as Button
//        book.setOnClickListener{ root ->
//            root.findNavController().navigate(R.id.action_nav_s_room_to_SRoomBookFragment)
//            root.findNavController().navigate(SRoomFragmentDirections.actionNavSRoomToSRoomBookFragment())
//        }

//        R.id.btn_Book.setOnClickListener{
//
//        }

        return root
    }

    fun loadRooms(){
        showProgressDialog()
        FirestoreClass().getRooms(this)
    }

    fun successLoadRooms(myRooms: ArrayList<Rooms>){
        hideProgressDialog()
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.rv_room)
        recyclerView.adapter = RoomAdapter(this, myRooms)
        recyclerView.setHasFixedSize(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun endPage(){
//        val action = SRoomFragmentDirections.actionNavSRoomToSRoomBookFragment()
//        action.roomId =
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(SRoomViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}
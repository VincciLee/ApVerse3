package com.example.apverse.ui.student.room

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.adapter.RoomAdapter
import com.example.apverse.databinding.FragmentSRoomBinding
import com.example.apverse.firestore.Firestore
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.firestore.Room
import com.example.apverse.model.Rooms
import com.example.apverse.ui.BaseFragment
import com.example.apverse.ui.student.book.SBookViewModel
import kotlinx.coroutines.launch


class SRoomFragment : BaseFragment() {

//    companion object {
//        fun newInstance() = SRoomFragment()
//    }
//

    private var _binding: FragmentSRoomBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SRoomViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SRoomViewModel::class.java)

        // Bind to Book Fragment
        _binding = FragmentSRoomBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inflate Room Booking xml
        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_s_room_book, null)

        (requireActivity() as MainActivity).supportActionBar?.title = "Discussion Room"

        viewLifecycleOwner.lifecycleScope.launch {
            loadRooms()
        }

        return root
    }

    suspend fun loadRooms(){
        showProgressDialog()
//        FirestoreClass().getRooms(this)
        viewModel.getMyRoomDetails()
        val roomList = viewModel.roomList

        successLoadRooms(roomList)
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
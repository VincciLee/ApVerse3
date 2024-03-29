package com.example.apverse.ui.student.room

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.adapter.RoomAdapter
import com.example.apverse.databinding.FragmentSRoomBinding
import com.example.apverse.model.Rooms
import com.example.apverse.ui.BaseFragment
import kotlinx.coroutines.launch


class SRoomFragment : BaseFragment() {

    private var _binding: FragmentSRoomBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SRoomViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SRoomViewModel::class.java)

        _binding = FragmentSRoomBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (requireActivity() as MainActivity).supportActionBar?.title = "Discussion Room"

        viewLifecycleOwner.lifecycleScope.launch {
            loadRooms()
        }

        return root
    }

    suspend fun loadRooms(){
        showProgressDialog()

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

}
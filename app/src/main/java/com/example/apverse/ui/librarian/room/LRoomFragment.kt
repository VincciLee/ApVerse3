package com.example.apverse.ui.librarian.room

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.apverse.R

class LRoomFragment : Fragment() {

    companion object {
        fun newInstance() = LRoomFragment()
    }

    private lateinit var viewModel: LRoomViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_l_room, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LRoomViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
package com.example.apverse.ui.librarian.book

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.apverse.R

class LBookFragment : Fragment() {

    companion object {
        fun newInstance() = LBookFragment()
    }

    private lateinit var viewModel: LBookViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_l_book, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LBookViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
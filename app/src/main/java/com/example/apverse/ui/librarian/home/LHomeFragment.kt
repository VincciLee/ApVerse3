package com.example.apverse.ui.librarian.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.databinding.FragmentLHomeBinding
import com.example.apverse.ui.BaseFragment
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class LHomeFragment : BaseFragment() {

    private var _binding: FragmentLHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LHomeViewModel::class.java)

        _binding = FragmentLHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (requireActivity() as MainActivity).supportActionBar?.title = "Homepage"

        viewLifecycleOwner.lifecycleScope.launch {
            getBookingCount()
            getReservationCount()
        }

        return root
    }

    private suspend fun getBookingCount() {
        val current = Calendar.getInstance().time
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
        val today = dateFormatter.format(current)

        viewModel.getRoomBookingCount(today)
        val count = viewModel.roomBookingCount
        successGetBookingCount(count)
    }

    fun successGetBookingCount(count: Int) {
        binding.textLHomeBook.text = count.toString()
    }

    private suspend fun getReservationCount() {
        viewModel.getBookReservationCount()
        val count = viewModel.bookReservationCount
        successGetReservationCount(count)
    }

    fun successGetReservationCount(count: Int) {
        binding.textLHomeReservation.text = count.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
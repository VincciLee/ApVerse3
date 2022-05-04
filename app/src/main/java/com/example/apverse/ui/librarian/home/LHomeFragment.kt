package com.example.apverse.ui.librarian.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.apverse.R
import com.example.apverse.databinding.FragmentLHomeBinding

class LHomeFragment : Fragment() {

    private var _binding: FragmentLHomeBinding? = null
    private val binding get() = _binding!!

//    companion object {
//        fun newInstance() = LHomeFragment()
//    }
//
//    private lateinit var viewModel: LHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val lHomeViewModel = ViewModelProvider(this).get(LHomeViewModel::class.java)

        _binding = FragmentLHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textLHome
        lHomeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(LHomeViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}
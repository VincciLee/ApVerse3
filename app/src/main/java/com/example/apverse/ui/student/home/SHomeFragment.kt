package com.example.apverse.ui.student.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.apverse.R
import com.example.apverse.databinding.FragmentSHomeBinding
import com.example.apverse.firestore.FirestoreClass
import com.google.android.material.navigation.NavigationView

class SHomeFragment : Fragment() {

    private var _binding: FragmentSHomeBinding? = null
    private val binding get() = _binding!!

//    companion object {
//        fun newInstance() = SHomeFragment()
//    }
//
//    private lateinit var viewModel: SHomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val sHomeViewModel =
            ViewModelProvider(this).get(SHomeViewModel::class.java)

        _binding = FragmentSHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textSHome
        sHomeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        binding.textSHome2.text = FirestoreClass().getCurrentUserEmail()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(SHomeViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}
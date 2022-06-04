package com.example.apverse.ui.login.usermenu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.apverse.databinding.FragmentUserMenuBinding

class UserMenuFragment : Fragment() {

    private var _binding: FragmentUserMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnLibrarian.setOnClickListener {
            it.findNavController().navigate(UserMenuFragmentDirections.actionUserMenuFragmentToLoginFragment("librarian"))
        }

        binding.btnStudent.setOnClickListener {
            it.findNavController().navigate(UserMenuFragmentDirections.actionUserMenuFragmentToLoginFragment("student"))
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
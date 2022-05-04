package com.example.apverse.ui.login.usermenu

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.apverse.R
import com.example.apverse.databinding.FragmentUserMenuBinding

class UserMenuFragment : Fragment() {

    companion object {
        fun newInstance() = UserMenuFragment()
    }

    private lateinit var viewModel: UserMenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentUserMenuBinding>(inflater,
            R.layout.fragment_user_menu, container, false)

        binding.btnLibrarian.setOnClickListener { root ->
            root.findNavController().navigate(UserMenuFragmentDirections.actionUserMenuFragmentToLoginFragment("librarian"))
        }

        binding.btnStudent.setOnClickListener { root ->
            root.findNavController().navigate(UserMenuFragmentDirections.actionUserMenuFragmentToLoginFragment("student"))
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserMenuViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
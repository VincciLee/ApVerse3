package com.example.apverse.ui.librarian.book

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.databinding.FragmentLBookDetailsBinding
import com.example.apverse.ui.BaseFragment

class LBookDetailsFragment : BaseFragment() {

    private var _binding: FragmentLBookDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: LBookDetailsFragmentArgs by navArgs()

    private var bookTitle: String = ""
    private var bookImage: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLBookDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (requireActivity() as MainActivity).supportActionBar?.title = "Book Details"

        binding.textLBookDetails.text = args.bookId

        return root
    }

}
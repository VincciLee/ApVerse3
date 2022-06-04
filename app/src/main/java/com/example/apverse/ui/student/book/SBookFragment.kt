package com.example.apverse.ui.student.book

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.adapter.BookAdapter
import com.example.apverse.databinding.FragmentSBookBinding
import com.example.apverse.model.Books
import com.example.apverse.ui.BaseFragment
import kotlinx.coroutines.launch

class SBookFragment : BaseFragment() {

    private lateinit var viewModel: SBookViewModel

    private var _binding: FragmentSBookBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        viewModel = ViewModelProvider(this).get(SBookViewModel::class.java)

        _binding = FragmentSBookBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_s_book, null)

        (requireActivity() as MainActivity).supportActionBar?.title = "Library Book"

        viewLifecycleOwner.lifecycleScope.launch {
            loadBooks()
        }

        return root
    }

    suspend fun loadBooks(){
        showProgressDialog()

        viewModel.getBookList()
        val bookList = viewModel.bookList
        successLoadBooks(bookList)
    }

    fun successLoadBooks(myBooks: ArrayList<Books>){
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.rv_s_book)

        hideProgressDialog()
        recyclerView.adapter = BookAdapter(this, myBooks)
        recyclerView.layoutManager = GridLayoutManager(this.context, 2)
        recyclerView.setHasFixedSize(true)
    }

    fun reload() {
        this.findNavController().navigate(SBookFragmentDirections.actionNavSBookSelf())
    }

}
package com.example.apverse.ui.librarian.book

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.adapter.BookAdapter
import com.example.apverse.databinding.FragmentLBookBinding
import com.example.apverse.model.Books
import com.example.apverse.ui.BaseFragment
import kotlinx.coroutines.launch

class LBookFragment : BaseFragment() {

    private lateinit var viewModel: LBookViewModel

    private var _binding: FragmentLBookBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LBookViewModel::class.java)

        _binding = FragmentLBookBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_l_book, null)

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
        hideProgressDialog()
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.rv_l_book)
        recyclerView.adapter = BookAdapter(this, myBooks)
        recyclerView.layoutManager = GridLayoutManager(this.context, 2)
        recyclerView.setHasFixedSize(true)
    }

}
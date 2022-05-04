package com.example.apverse.ui.student.book

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.adapter.BookAdapter
import com.example.apverse.adapter.ComputerAdapter
import com.example.apverse.databinding.FragmentSBookBinding
import com.example.apverse.databinding.FragmentSComputerBinding
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.Books
import com.example.apverse.model.Computers
import com.example.apverse.ui.BaseFragment

class SBookFragment : BaseFragment() {

//    companion object {
//        fun newInstance() = SBookFragment()
//    }
//
//    private lateinit var viewModel: SBookViewModel

    private var _binding: FragmentSBookBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSBookBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_s_book, null)

        (requireActivity() as MainActivity).supportActionBar?.title = "Library Book"

        loadBooks()

        return root
    }

    fun loadBooks(){
        showProgressDialog()
        FirestoreClass().getBooks(this)
    }

    fun successLoadBooks(myBooks: ArrayList<Books>){
        hideProgressDialog()
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.rv_s_book)
        recyclerView.adapter = BookAdapter(this, myBooks)
        recyclerView.layoutManager = GridLayoutManager(this.context, 2)
        recyclerView.setHasFixedSize(true)
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(SBookViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}
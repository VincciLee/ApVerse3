package com.example.apverse.ui.student.computer

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.adapter.ComputerAdapter
import com.example.apverse.databinding.FragmentSComputerBinding
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.Computers
import com.example.apverse.model.Rooms
import com.example.apverse.ui.BaseFragment
import com.example.apverse.utils.Constants

class SComputerFragment : BaseFragment() {

//    companion object {
//        fun newInstance() = SComputerFragment()
//    }
//
//    private lateinit var viewModel: SComputerViewModel

    private var _binding: FragmentSComputerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSComputerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val inflater = layoutInflater
        val view: View = inflater.inflate(R.layout.fragment_s_computer, null)

        (requireActivity() as MainActivity).supportActionBar?.title = "Library Computer"

//        viewModel = ViewModelProvider(this).get(SComputerViewModel::class.java)

        loadComputers()

        return root
    }

    fun loadComputers(){
        showProgressDialog()
        FirestoreClass().getComputers(this)
    }

    fun successLoadComputers(myComputers: ArrayList<Computers>, isUsing: Boolean){
        hideProgressDialog()
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.rv_computer)
        recyclerView.adapter = ComputerAdapter(this, myComputers, isUsing)
        recyclerView.setHasFixedSize(true)
    }

    fun updateComputerStatus(computerHashmap: HashMap<String,Any>, checkingHashMap: HashMap<String,Any>){
        FirestoreClass().getDocumentId(this, Constants.COMPUTERS, computerHashmap, checkingHashMap)
    }

    fun successUpdateComputerStatus(){
        showErrorSnackBar("Computer status updated.", false)
        reload()
    }

    fun failedUpdateComputerStatus(){
        showErrorSnackBar("Computer status update failed.", true)
    }

    fun reload() {
        this.findNavController().navigate(R.id.action_nav_s_computer_self)
//        getFragmentManager()?.beginTransaction()?.detach(this)?.attach(this)?.commit()
//        requireActivity().supportFragmentManager.beginTransaction().detach(this).attach(this).commit()
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(SComputerViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}
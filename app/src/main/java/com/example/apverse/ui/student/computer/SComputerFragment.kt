package com.example.apverse.ui.student.computer

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.adapter.ComputerAdapter
import com.example.apverse.databinding.FragmentSComputerBinding
import com.example.apverse.model.Computers
import com.example.apverse.ui.BaseFragment
import kotlinx.coroutines.launch

class SComputerFragment : BaseFragment() {

    private lateinit var viewModel: SComputerViewModel

    private var _binding: FragmentSComputerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(SComputerViewModel::class.java)

        _binding = FragmentSComputerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        (requireActivity() as MainActivity).supportActionBar?.title = "Library Computer"

        viewLifecycleOwner.lifecycleScope.launch {
            loadComputers()
        }

        return root
    }

    suspend fun loadComputers(){
        showProgressDialog()

        viewModel.getComputerList()
        val computerList = viewModel.computerList
        val isUsing = viewModel.isUsing

        successLoadComputers(computerList, isUsing)
    }

    fun successLoadComputers(myComputers: ArrayList<Computers>, isUsing: Boolean){
        hideProgressDialog()
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.rv_computer)
        recyclerView.adapter = ComputerAdapter(this, myComputers, isUsing)
        recyclerView.setHasFixedSize(true)
    }

    suspend fun updateComputerStatus(docId: String, hashMap: HashMap<String, Any>){
        val result = viewModel.updateComputerStatus(docId, hashMap)

        if (result) {
            successUpdateComputerStatus()
        }
        else {
            failedUpdateComputerStatus()
        }
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
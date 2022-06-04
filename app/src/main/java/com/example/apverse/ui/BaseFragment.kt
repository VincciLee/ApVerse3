package com.example.apverse.ui

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.apverse.R
import com.google.android.material.snackbar.Snackbar

open class BaseFragment : Fragment() {
    private lateinit var mProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    fun showErrorSnackBar(message:String,errorMessage:Boolean){
        val snackBar=
            Snackbar.make(requireActivity().findViewById(android.R.id.content),message, Snackbar.LENGTH_LONG)
        val snackBarView=snackBar.view

        //set error bar color
        if(errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.dark_red
                )
            )
        }
        else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    requireActivity(),
                    R.color.dark_blue
                )
            )
        }
        snackBar.show()
    }

    fun showProgressDialog() {
        mProgressDialog = Dialog(requireActivity())

        mProgressDialog.setContentView(R.layout.progress_dialog)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()
    }

    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }

}
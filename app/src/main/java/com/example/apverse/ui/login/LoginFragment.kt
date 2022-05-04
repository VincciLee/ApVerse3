package com.example.apverse.ui.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.example.apverse.LoginActivity
import com.example.apverse.MainActivity
import com.example.apverse.R
import com.example.apverse.databinding.FragmentLoginBinding
import com.example.apverse.databinding.FragmentSRoomBookBinding
import com.example.apverse.ui.BaseFragment
import com.example.apverse.ui.student.room.SRoomBookFragmentArgs
import com.example.apverse.utils.Constants
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : BaseFragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val args: LoginFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val userType = args.userType
        binding.testUser.text = args.userType

        binding.btnLogin.setOnClickListener {
//                root ->
//            val intent = Intent(this@LoginFragment.requireContext(), MainActivity::class.java)
//            startActivity(intent)
            loginUser(userType)
        }

        return root
    }

    private fun validateLoginDetails(): Boolean{
        return when{
            TextUtils.isEmpty(binding.inputEmail.text.toString().trim{ it<=' ' })->{
                showErrorSnackBar("Please enter email.",true)
                false
            }
            TextUtils.isEmpty(binding.inputPassword.text.toString().trim{ it<=' ' })->{
                showErrorSnackBar("Please enter password.",true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun loginUser(userType: String){
        if(validateLoginDetails()){
            showProgressDialog()

            val email = binding.inputEmail.text.toString().trim{ it<=' ' }
            val password = binding.inputPassword.text.toString().trim{ it<=' ' }

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        showErrorSnackBar("Logged in successfully.",false)

                        Constants.USERTYPE = userType

                        val intent = Intent(this@LoginFragment.requireContext(), MainActivity::class.java)
                        startActivity(intent)
                    }
                    else{
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                    }
                }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
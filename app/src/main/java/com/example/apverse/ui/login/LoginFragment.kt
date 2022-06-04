package com.example.apverse.ui.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.example.apverse.LoginActivity
import com.example.apverse.MainActivity
import com.example.apverse.databinding.FragmentLoginBinding
import com.example.apverse.ui.BaseFragment
import com.example.apverse.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginFragment : BaseFragment() {

    private lateinit var viewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val args: LoginFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val userType = args.userType

        binding.btnLogin.setOnClickListener {
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
                        if (Constants.USERTYPE == userType) {
                            viewLifecycleOwner.lifecycleScope.launch {
                                val result = viewModel.getUserInfo(userType)

                                if(result) {
                                    successLogin(userType)
                                }
                                else {
                                    hideProgressDialog()
                                    showErrorSnackBar("The user is not librarian.",true)
                                }
                            }
                        }
                        else {
                            successLogin(userType)
                        }
                    }
                    else{
                        hideProgressDialog()
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                    }
                }
        }
    }

    private fun successLogin(userType: String) {
        hideProgressDialog()
        showErrorSnackBar("Logged in successfully.",false)

        Constants.USERTYPE = userType

        val intent = Intent(this@LoginFragment.requireContext(), MainActivity::class.java)
        startActivity(intent)
        (requireActivity() as LoginActivity).finish()
    }

}
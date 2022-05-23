package com.example.apverse.ui.login

import androidx.lifecycle.ViewModel
import com.example.apverse.firestore.Firestore
import com.example.apverse.model.Users

class LoginViewModel : ViewModel() {

    var userInfo: Users = Users()

    suspend fun getUserInfo(userType: String) : Boolean {
        val data = Firestore().getUserInfo()
        for (i in data?.documents!!){
            userInfo = i?.toObject(Users::class.java)!!
        }

        return userInfo.user_type == userType
    }
}
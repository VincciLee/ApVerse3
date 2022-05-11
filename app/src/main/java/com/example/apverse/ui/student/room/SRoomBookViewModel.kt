package com.example.apverse.ui.student.room

import androidx.lifecycle.ViewModel
import com.example.apverse.firestore.Firestore
import com.example.apverse.model.Users
import com.google.firebase.firestore.auth.User

class SRoomBookViewModel: ViewModel() {

    var userInfo: Users = Users()
    var userTp: String = ""
    var userNum: String = ""

    suspend fun getUserInfo() {
        val data = Firestore().getUserInfo()
        for (i in data?.documents!!){
            userInfo = i?.toObject(Users::class.java)!!
        }

        userTp = userInfo.user_email.substring(0, 2).uppercase()
        userNum = userInfo.user_email.substringBefore("@").substringAfter("tp")
    }
}
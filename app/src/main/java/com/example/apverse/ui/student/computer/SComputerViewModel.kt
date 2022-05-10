package com.example.apverse.ui.student.computer

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.apverse.firestore.Firestore
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.Computers

class SComputerViewModel: ViewModel() {

    var computerList: ArrayList<Computers> = ArrayList()
    var isUsing: Boolean = false

    suspend fun getComputerList() {
        val data = Firestore().getComputerList()
        for (i in data?.documents!!){
            val computer = i?.toObject(Computers::class.java)!!
            computer.doc_id = i.id
            computerList.add(computer)

            if(computer.student_email == Firestore().getCurrentUserEmail()){
                isUsing = true
            }
        }
    }

    suspend fun updateComputerStatus(docId: String, hashMap: HashMap<String, Any>) : Boolean {
        return Firestore().updateComputerStatus(docId, hashMap)
    }
}
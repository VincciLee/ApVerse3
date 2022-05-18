package com.example.apverse.adapter

import android.service.autofill.Dataset
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.R
import com.example.apverse.firestore.Firestore
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.Computers
import com.example.apverse.ui.student.computer.SComputerFragment
import com.example.apverse.utils.Constants
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class ComputerAdapter(
        private val fragment: SComputerFragment,
        private val dataset: ArrayList<Computers>,
        private val isUsing: Boolean
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

        class ComputerViewHolder(private val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return ComputerViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.computer_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
         if(holder is ComputerViewHolder){
             val item = dataset[position]
             val docId = item.doc_id
             val computerId = item.computer_id
             val status = item.computer_status
             val computerUser = item.student_email
             val computerHashmap: HashMap<String, Any> = HashMap()
             val checkingHashMap: HashMap<String, Any> = HashMap()

             holder.itemView.findViewById<TextView>(R.id.text_computer).text = "Computer $computerId"
             Log.i("ApVerse::CompAdapter", "docId: $docId")

             if(isUsing == true){
                 if(computerUser == Firestore().getCurrentUserEmail()){
                     holder.itemView.findViewById<MaterialButton>(R.id.btn_computer).text = "Finish"
                 }else{
                     if(status == Constants.FREE){
                         holder.itemView.findViewById<MaterialButton>(R.id.btn_computer).text = "USE"
                     }else if(status == Constants.IN_USE){
                         holder.itemView.findViewById<MaterialButton>(R.id.btn_computer).text = Constants.IN_USE
                     }

                     holder.itemView.findViewById<MaterialButton>(R.id.btn_computer).isEnabled = false
                 }
             }else{
                 if(status == Constants.FREE){
                     holder.itemView.findViewById<MaterialButton>(R.id.btn_computer).text = "USE"
                 }else if(status == Constants.IN_USE){
                     holder.itemView.findViewById<MaterialButton>(R.id.btn_computer).text = Constants.IN_USE
                     holder.itemView.findViewById<MaterialButton>(R.id.btn_computer).isEnabled = false
                 }
             }

             holder.itemView.findViewById<MaterialButton>(R.id.btn_computer).setOnClickListener {
                 Log.i("ApVerse::CompAdapter", "btn_computer on clicked")

                 if(isUsing == true) {
                     if (computerUser == Firestore().getCurrentUserEmail()) {
                         Log.i("ApVerse::CompAdapter", "isUsing == true")
                         computerHashmap[Constants.COMPUTER_STATUS] = Constants.FREE
                         computerHashmap[Constants.STUDENT_EMAIL] = ""

//                         checkingHashMap[Constants.STUDENT_EMAIL] = FirestoreClass().getCurrentUserEmail()
                     }
                 }else{
                     Log.i("ApVerse::CompAdapter", "isUsing == false")
                     computerHashmap[Constants.COMPUTER_STATUS] = Constants.IN_USE
                     computerHashmap[Constants.STUDENT_EMAIL] = Firestore().getCurrentUserEmail()

//                     checkingHashMap[Constants.COMPUTER_ID] = computerId
                 }

                 fragment.viewLifecycleOwner.lifecycleScope.launch {
                     fragment.updateComputerStatus(docId, computerHashmap)
                 }

//                 Firestore().get
//                 FirestoreClass().updateComputerStatus(fragment, computerHashmap, docId)
//                 FirestoreClass().getDocumentId(fragment, Constants.COMPUTERS, computerHashmap, checkingHashMap)
             }
         }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}
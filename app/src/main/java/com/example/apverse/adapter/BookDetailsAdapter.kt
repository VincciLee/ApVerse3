package com.example.apverse.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.R
import com.example.apverse.firestore.FirestoreClass
import com.example.apverse.model.BookReservation
import com.example.apverse.ui.student.book.SBookDetailsFragment
import com.example.apverse.utils.Constants

class BookDetailsAdapter(
    private val fragment: SBookDetailsFragment,
    private val dataset: ArrayList<BookReservation>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class BookDetailsViewHolder(private val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return BookDetailsViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.book_reservation_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.i("ApVerse::DetailsAdapter", "in onBindViewHolder()")

        if (holder is BookDetailsViewHolder) {
            val item = dataset[position]
            val docId = item.doc_id
            val studentEmail = item.student_email
            val studentName = item.student_name
            val date = item.date
            val status = item.reservation_status
            val isReady = item.ready

            Log.i("ApVerse::DetailsAdapter", "studentEmail: $studentEmail")
            Log.i("ApVerse::DetailsAdapter", "loginEmail: "+FirestoreClass().getCurrentUserEmail())
            Log.i("ApVerse::DetailsAdapter", "isReady: ${isReady}")

            holder.itemView.findViewById<TextView>(R.id.text_reserved_student).text = studentName
            holder.itemView.findViewById<TextView>(R.id.text_reserved_date).text = date
            holder.itemView.findViewById<TextView>(R.id.text_reserved_status).text = status

            if(studentEmail == FirestoreClass().getCurrentUserEmail()) {
                if(isReady == true) {
                    holder.itemView.findViewById<TextView>(R.id.text_reserved_status).text = Constants.IS_READY
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}
package com.example.apverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.R
import com.example.apverse.model.BookReservation
import com.example.apverse.utils.Constants

class BookDetailsAdapter(
    private val fragment: Fragment,
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
        if (holder is BookDetailsViewHolder) {
            val item = dataset[position]
            val docId = item.doc_id
            val studentEmail = item.student_email
            val studentName = item.student_name
            val date = item.date
            val status = item.reservation_status
            val isReady = item.ready

            holder.itemView.findViewById<TextView>(R.id.text_reserved_student).text = studentName
            holder.itemView.findViewById<TextView>(R.id.text_reserved_date).text = date

            if(isReady == true) {
                holder.itemView.findViewById<TextView>(R.id.text_reserved_status).text = Constants.IS_READY
            }
            else {
                holder.itemView.findViewById<TextView>(R.id.text_reserved_status).text = status
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}
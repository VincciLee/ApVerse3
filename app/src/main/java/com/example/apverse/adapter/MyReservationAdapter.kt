package com.example.apverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.R
import com.example.apverse.model.BookReservation
import com.example.apverse.ui.student.home.SHomeFragment
import com.example.apverse.utils.Constants

class MyReservationAdapter(
    private val fragment: SHomeFragment,
    private  val dataset: ArrayList<BookReservation>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class myReservationViewHolder(private val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return myReservationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.my_reservation_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is myReservationViewHolder) {
            val item = dataset[position]
            val bookTitle = item.book_title
            val isReady = item.ready
            var status: String = ""

            if(isReady) {
                status = Constants.IS_READY
            }
            else{
                status = Constants.PENDING
            }

            holder.itemView.findViewById<TextView>(R.id.text_s_home_title).text = bookTitle
            holder.itemView.findViewById<TextView>(R.id.text_s_home_status).text = status
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}
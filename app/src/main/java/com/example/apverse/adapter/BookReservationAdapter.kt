package com.example.apverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.R
import com.example.apverse.model.BookReservation
import com.example.apverse.ui.librarian.book.LBookReservationFragment
import com.example.apverse.utils.Constants
import com.example.apverse.utils.GlideLoader
import kotlinx.coroutines.launch

class BookReservationAdapter(
    private val fragment: LBookReservationFragment,
    private val dataset: ArrayList<BookReservation>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class BookReservationViewHolder(private val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return BookReservationViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.reservation_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BookReservationViewHolder) {
            val item = dataset[position]
            val docId = item.doc_id
            val bookId = item.book_id
            val bookTitle = item.book_title
            val bookImage = item.book_image
            val isReady = item.ready
            val name = item.student_name
            val date = item.date
            var time = item.time
            val reservationHashMap: HashMap<String, Any> = HashMap()

            if (time.substringBefore(":").toInt() < 12) {
                time = "$time am"
            }
            else {
                time = "$time pm"
            }

            holder.itemView.findViewById<TextView>(R.id.text_reservation_title).text = bookTitle
            holder.itemView.findViewById<TextView>(R.id.text_reservation_id).text = bookId
            holder.itemView.findViewById<TextView>(R.id.text_reservation_name).text = name
            holder.itemView.findViewById<TextView>(R.id.text_reservation_date).text = "$date  $time"

            GlideLoader(fragment).loadImage(bookImage,holder.itemView.findViewById<ImageView>(R.id.img_reservation_book), 80)

            if (isReady == true) {
                holder.itemView.findViewById<Button>(R.id.btn_alert).text = "Pickup"
            }else {
                holder.itemView.findViewById<Button>(R.id.btn_alert).text = "Ready"
            }

            holder.itemView.findViewById<Button>(R.id.btn_alert).setOnClickListener { root ->
                if (isReady == true) {
                    fragment.viewLifecycleOwner.lifecycleScope.launch {
                        fragment.deleteReservation(docId)
                    }
                }
                else {
                    reservationHashMap[Constants.READY] = true

                    fragment.viewLifecycleOwner.lifecycleScope.launch {
                        fragment.validateReservation(docId, bookId, reservationHashMap)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}
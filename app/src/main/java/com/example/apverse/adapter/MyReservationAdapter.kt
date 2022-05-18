package com.example.apverse.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.R
import com.example.apverse.model.BookReservation
import com.example.apverse.ui.student.home.SHomeFragment
import com.example.apverse.utils.Constants
import kotlinx.coroutines.launch

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
            val docId = item.doc_id
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

            holder.itemView.findViewById<Button>(R.id.btn_s_home_cancel).setOnClickListener {
                val builder = AlertDialog.Builder(fragment.context)
                builder.setMessage("Are you sure you want to cancel the book reservation?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        fragment.viewLifecycleOwner.lifecycleScope.launch {
                            fragment.cancelReservation(docId)
                        }
                    }
                    .setNegativeButton("No") { dialog, id ->
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }


}
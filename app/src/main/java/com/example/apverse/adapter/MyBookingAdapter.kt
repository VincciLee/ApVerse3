package com.example.apverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.R
import com.example.apverse.model.RoomBooking
import com.example.apverse.ui.student.home.SHomeFragment
import com.example.apverse.ui.student.home.SHomeFragmentDirections

class MyBookingAdapter(
    private val fragment: SHomeFragment,
    private val dataset: ArrayList<RoomBooking>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class MyBookingViewHolder(private val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MyBookingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.my_booking_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MyBookingViewHolder) {
            val item = dataset[position]
            val docId = item.doc_id
            val roomId = item.room_id
            val date = item.date
            var time = item.time

            if(time.substringBefore(":").toInt() < 12) {
                time = "$time am"
            }
            else {
                time = "$time pm"
            }

            holder.itemView.findViewById<TextView>(R.id.text_s_home_room).text = "Room $roomId"
            holder.itemView.findViewById<TextView>(R.id.text_s_home_date).text = "$date  $time"

            holder.itemView.findViewById<Button>(R.id.btn_s_home_edit).setOnClickListener { root ->
                fragment.findNavController().navigate(SHomeFragmentDirections.actionNavSHomeToSMyRoomFragment(docId, roomId))
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}
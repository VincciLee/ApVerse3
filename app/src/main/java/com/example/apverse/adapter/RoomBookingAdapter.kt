package com.example.apverse.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.R
import com.example.apverse.model.RoomBooking
import com.example.apverse.ui.librarian.room.LRoomFragment

class RoomBookingAdapter(
    private val fragment: LRoomFragment,
    private val dataset: ArrayList<RoomBooking>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class RoomBookingViewHolder(private val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return RoomBookingViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.room_booking_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.i("ApVerse::BookingAdapter", "Enter onBindViewHolder()")

        if (holder is RoomBookingViewHolder) {
            val item = dataset[position]
            val roomId = item.room_id
            val date = item.date
            var time = item.time
            val name = item.student_name
            val hdmi = item.hdmi_cable

//            var dbTpNumber = item.student_tp.substringBefore("@")
//            var dbTp = dbTpNumber.substring(0, 2).uppercase()
//            var dbNumber = dbTpNumber.substringAfter("tp")
            var tpNumber = item.student_tp

            if(time.substringBefore(":").toInt() >= 12){
                time = time+" pm"
            }
            else{
                time = time+" am"
            }

            holder.itemView.findViewById<TextView>(R.id.text_room_id).text = "Discussom Room ${roomId}"
            holder.itemView.findViewById<TextView>(R.id.text_date).text = "$date"
            holder.itemView.findViewById<TextView>(R.id.text_name).text = "$name"
            holder.itemView.findViewById<TextView>(R.id.text_tp).text = "$tpNumber"
            holder.itemView.findViewById<TextView>(R.id.text_time).text = "$time"

            if(hdmi){
                holder.itemView.findViewById<TextView>(R.id.text_cable).text = "HDMI cable requested"
            }
            else{
                holder.itemView.findViewById<TextView>(R.id.text_cable).text = ""
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}
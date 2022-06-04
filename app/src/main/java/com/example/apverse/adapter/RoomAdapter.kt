package com.example.apverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.R
import com.example.apverse.model.Rooms
import com.example.apverse.ui.student.room.SRoomFragment
import com.example.apverse.ui.student.room.SRoomFragmentDirections

class RoomAdapter(
    private val fragment: SRoomFragment,
    private val dataset: ArrayList<Rooms>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class RoomViewHolder(private val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return RoomViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.room_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is RoomViewHolder){
            val item = dataset[position]
            val roomId = item.room_id
            val capacity = item.capacity

            holder.itemView.findViewById<TextView>(R.id.text_s_room).text = " Room $roomId"
            holder.itemView.findViewById<TextView>(R.id.text_s_capacity).text = " Capacity: $capacity"

            holder.itemView.findViewById<Button>(R.id.btn_Book).setOnClickListener{ root ->
                fragment.findNavController().navigate(SRoomFragmentDirections.actionNavSRoomToSRoomBookFragment(roomId, capacity))
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}



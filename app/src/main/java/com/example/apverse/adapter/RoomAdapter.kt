package com.example.apverse.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.R
import com.example.apverse.model.Rooms
import com.example.apverse.ui.student.room.SRoomFragment
import com.example.apverse.ui.student.room.SRoomFragmentDirections

class RoomAdapter(
    private val fragment: SRoomFragment,
    private val dataset: ArrayList<Rooms>
//) : RecyclerView.Adapter<RoomAdapter.ItemViewHolder>() {
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

//    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
//        val textSRoom: TextView = view.findViewById(R.id.text_s_room)
//        val textSCapacity: TextView = view.findViewById(R.id.text_s_capacity)
//    }

    class RoomViewHolder(private val view: View): RecyclerView.ViewHolder(view)

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        val adapterLayout = LayoutInflater.from(parent.context)
//        val adapterLayout = LayoutInflater.from(parent.context)
//            .inflate(R.layout.fragment_s_room, parent, false)
//        return ItemViewHolder(adapterLayout)
        return RoomViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.room_list,
                parent,
                false
            )
        )
    }

//    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is RoomViewHolder){
            Log.i("ApVerse", "In Room Adapter now")

            val item = dataset[position]
//            val roomId = item.stringResourceId
            val roomId = item.room_id
            val capacity = item.capacity

            Log.i("ApVerse", "Adapter: $roomId, $capacity")

            holder.itemView.findViewById<TextView>(R.id.text_s_room).text = " Room $roomId"
            holder.itemView.findViewById<TextView>(R.id.text_s_capacity).text = " Capacity: $capacity"

            holder.itemView.findViewById<Button>(R.id.btn_Book).setOnClickListener{ root ->
//                val action = fragment.action_nav_s_room_to_SRoomBookFragment()
//                val action = SRoomFragmentDirections.actionNavSRoomToSRoomBookFragment()
//                action.roomId = roomId
//                action.arguments

//                fragment.findNavController().navigate(R.id.action_nav_s_room_to_SRoomBookFragment)
                fragment.findNavController().navigate(SRoomFragmentDirections.actionNavSRoomToSRoomBookFragment(roomId, capacity))
            }
        }
//        val item = dataset[position]
//        holder.textSRoom.text = fragment.resources.getString(item.stringResourceId)
//        holder.textSCapacity.text = fragment.resources.getString(item.capacity)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}



package com.example.apverse.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.apverse.R
import com.example.apverse.model.Books
import com.example.apverse.ui.student.book.SBookFragment
import com.example.apverse.ui.student.book.SBookFragmentDirections

class BookAdapter(
    private val fragment: SBookFragment,
    private val dataset: ArrayList<Books>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class BookViewHolder(private val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return BookViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.book_list,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BookViewHolder){
            val item = dataset[position]
            val docId = item.doc_id
            val bookTitle = item.book_title
            val bookAuthor = item.book_author
            val bookStatus = item.book_status
            val bookImage = item.book_image
            val bookId = item.book_id

            holder.itemView.findViewById<TextView>(R.id.text_s_book_title).text = bookTitle
            holder.itemView.findViewById<TextView>(R.id.text_s_book_author).text = bookAuthor
            holder.itemView.findViewById<TextView>(R.id.text_s_book_status).text = bookStatus

            Glide.with(fragment)
                .load(bookImage)
                .apply(RequestOptions.placeholderOf(R.drawable.unknown)
                    .override(100,100))
                .into(holder.itemView.findViewById<ImageView>(R.id.s_book_img))

            holder.itemView.findViewById<Button>(R.id.btn_view).setOnClickListener { root ->
                fragment.findNavController().navigate(SBookFragmentDirections.actionNavSBookToSBookDetailsFragment(bookId))
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}
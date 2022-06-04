package com.example.apverse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.apverse.R
import com.example.apverse.model.Books
import com.example.apverse.ui.librarian.book.LBookFragment
import com.example.apverse.ui.librarian.book.LBookFragmentDirections
import com.example.apverse.ui.student.book.SBookFragment
import com.example.apverse.ui.student.book.SBookFragmentDirections
import com.example.apverse.utils.GlideLoader

class BookAdapter(
    private val fragment: Fragment,
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

            when (fragment) {
                is SBookFragment -> {
                    GlideLoader(fragment).loadImage(bookImage,holder.itemView.findViewById<ImageView>(R.id.s_book_img), 100)

                    holder.itemView.findViewById<Button>(R.id.btn_view).setOnClickListener { root ->
                        fragment.findNavController().navigate(SBookFragmentDirections.actionNavSBookToSBookDetailsFragment(bookId))
                    }
                }

                is LBookFragment -> {
                    GlideLoader(fragment).loadImage(bookImage,holder.itemView.findViewById<ImageView>(R.id.s_book_img), 100)

                    holder.itemView.findViewById<Button>(R.id.btn_view).setOnClickListener { root ->
                        fragment.findNavController().navigate(LBookFragmentDirections.actionNavLBookToLBookDetailsFragment(bookId))
                    }
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}
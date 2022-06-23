package com.example.henripotierlibtest

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.henripotierlibtest.books.Book
import com.example.henripotierlibtest.databinding.AdapterBookBinding

class BookAdapterNew : RecyclerView.Adapter<BookAdapterNew.ListViewHolder>() {

    var bookList = mutableListOf<Book>()
    var tracker: SelectionTracker<Book>? = null

    fun setMovies(books: List<Book>) {
        this.bookList = books.toMutableList()
        notifyDataSetChanged()
    }
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postImageView: ImageView = itemView.findViewById(R.id.imageview)
        val name: TextView = itemView.findViewById(R.id.name)
        val price: TextView = itemView.findViewById(R.id.price)

        fun setPostImage(book: Book, isActivated: Boolean = false) {
            //postImageView.setImageResource(postItem.image)
            Glide.with(postImageView.context).load(book.cover ).into(postImageView)
            name.text = book.title
            price.text = ""+book.price+" €"
            itemView.isActivated = isActivated
            if(isActivated){
                postImageView.setBackgroundColor(Color.parseColor("#797EF6"))
            }
            else{
                postImageView.setBackgroundColor(Color.TRANSPARENT)

            }

        }
        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Book> =
            object : ItemDetailsLookup.ItemDetails<Book>() {
                override fun getPosition(): Int = adapterPosition
                override fun getSelectionKey(): Book? = bookList[position]
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        //val binding = AdapterBookBinding.inflate(inflater, parent, false)
        //return ListViewHolder(binding)
        val itemView = inflater.inflate(R.layout.adapter_book, parent, false)
        return ListViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val book = bookList[position]
        //if (ValidationUtil.validateMovie(movie)) {
         //   holder.binding.name.text = book.title
         //   Glide.with(holder.itemView.context).load(book.cover ).into(holder.binding.imageview)
        //}

        //holder.setPostImage(bookList[position], it.isSelected(postItems[position]))
        tracker?.let {
            Log.e("PostsAdapter","onBindViewHolder called it.isSelected = "+it.isSelected(bookList[position])+"\t position = "+position)
            holder.setPostImage(bookList[position], it.isSelected(bookList[position]))


        }
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    fun getItem(position: Int) = bookList[position]
    fun getPosition(p_isbn: String) = bookList.indexOfFirst { it.isbn == p_isbn }

}

class MainViewHolderNEw(val binding: AdapterBookBinding) : RecyclerView.ViewHolder(binding.root) {

}
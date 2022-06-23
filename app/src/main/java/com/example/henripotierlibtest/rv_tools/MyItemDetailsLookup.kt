package com.example.henripotierlibtest.rv_tools
import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import com.example.henripotierlibtest.books.Book
import com.example.henripotierlibtest.BookAdapterNew

class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Book>() {
    override fun getItemDetails(event: MotionEvent): ItemDetails<Book>? {
        val view = recyclerView.findChildViewUnder(event.x, event.y)
        if (view != null) {
            return (recyclerView.getChildViewHolder(view) as BookAdapterNew.ListViewHolder)
                .getItemDetails()
        }
        return null
    }
}
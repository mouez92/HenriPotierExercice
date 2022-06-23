package com.example.henripotierlibtest.rv_tools

import androidx.recyclerview.selection.ItemKeyProvider
import com.example.henripotierlibtest.books.Book
import com.example.henripotierlibtest.BookAdapterNew

class MyItemKeyProvider(private val adapter: BookAdapterNew) :
    ItemKeyProvider<Book>(SCOPE_CACHED) {
    override fun getKey(position: Int): Book? {
        return adapter.getItem(position)
    }

    override fun getPosition(key: Book): Int {
        return adapter.getPosition(key.isbn!!)
    }
}
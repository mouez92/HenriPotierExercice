package com.example.henripotierlibtest.books

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Book(
    val cover: String,
    val isbn: String,
    val price: Int,
    val synopsis: List<String>,
    val title: String
): Parcelable

/*
fun BookDto.toBook(): Book {
    return Book(
        cover = cover,
        isbn = isbn,
        price = price,
        synopsis = synopsis,
        title = title
    )
}

 */
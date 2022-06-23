package com.example.henripotierlibtest.offers

data class Offer(
    val type: String,
    val sliceValue: Int? = null,
    val value: Int
)
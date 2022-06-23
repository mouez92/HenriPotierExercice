package com.example.henripotierlibtest.repository

import com.example.henripotierlibtest.di.RetrofitService

class MainRepository constructor(private val retrofitService: RetrofitService) {

    suspend fun getAllBooks() = retrofitService.getAllBooks()

    suspend fun getCommercialOffers(ISBNs: String) = retrofitService.getCommercialOffers(ISBNs)

}
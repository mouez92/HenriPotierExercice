package com.example.henripotierlibtest.di

import com.example.henripotierlibtest.books.Book
import com.example.henripotierlibtest.offers.OfferResult
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {

    @GET("/books")
    suspend fun getAllBooks() : Response<List<Book>>

    @GET("/books/{param_books_isbns}/commercialOffers")
    suspend fun getCommercialOffers(@Path("param_books_isbns") num : String) : Response<OfferResult>
    //suspend fun getCommercialOffers(@Path("param_books_isbns") num : String) : Response<List<Offer>>

    companion object {
        var retrofitService: RetrofitService? = null
        fun getInstance() : RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

    }
}
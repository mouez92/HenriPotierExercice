package com.example.henripotierlibtest.offers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.henripotierlibtest.repository.MainRepository

class OfferViewModelFactory
constructor(
    private val repository: MainRepository,
    private val  ISPNs: String
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(OfferViewModel::class.java)) {
            OfferViewModel(this.repository,ISPNs) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}
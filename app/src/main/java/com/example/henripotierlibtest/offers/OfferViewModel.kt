package com.example.henripotierlibtest.offers

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.henripotierlibtest.repository.MainRepository
import kotlinx.coroutines.*

class OfferViewModel
constructor(
    private val mainRepository: MainRepository,
    private val ISBNs: String

) : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    //val offerList = MutableLiveData<List<Offer>>()
    val offerList = MutableLiveData<OfferResult>()

    var job: Job? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }
    val loading = MutableLiveData<Boolean>()

    fun getAllOffers() {

        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            loading.postValue(true)
            val response = mainRepository.getCommercialOffers(ISBNs)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    offerList.postValue(response.body())
                    loading.value = false
                } else {
                    onError("Error : ${response} ")
                }
            }
        }

    }

    private fun onError(message: String) {
        //errorMessage.value = message
        //loading.value = false

        errorMessage.postValue(message)
        loading.postValue(false)

    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

}
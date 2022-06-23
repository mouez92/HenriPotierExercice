package com.example.henripotierlibtest.offers

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.example.henripotierlibtest.repository.MainRepository
import com.example.henripotierlibtest.di.RetrofitService
import com.example.henripotierlibtest.databinding.ActivityOfferBinding

class OfferActivity : AppCompatActivity() {
    private var selectedPostItems: MutableList<Offer> = mutableListOf()

    lateinit var viewModel: OfferViewModel
    //private val adapter = BookAdapter()

    lateinit var binding: ActivityOfferBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityOfferBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val retrofitService = RetrofitService.getInstance()
        val mainRepository = MainRepository(retrofitService)
        val ISBNs:String = intent.getStringExtra("ISBNs").toString()
        val price:Int = intent.getIntExtra("price",0)

        viewModel = ViewModelProvider(this, OfferViewModelFactory(mainRepository,ISBNs)).get(OfferViewModel::class.java)
        viewModel.offerList.observe(this) {
            Log.e("offerList","offerList size = "+it.toString())
            Log.e("offerList","offerList  = "+it)
            Log.e("offerList","price  = "+price)

            val offers= it.offers
            var pricefinal:Int =-1
            var type_offer: String=""
            for(offer in offers){
                if(offer.type.equals("percentage")){
                    if(pricefinal>0 &&pricefinal>price - offer.value) {
                        pricefinal = price - offer.value
                        type_offer ="percentage"+"("+offer.value+"%)"
                    }
                    if(pricefinal<0){
                        pricefinal = price - offer.value
                        type_offer ="percentage"+"("+offer.value+"%)"

                    }
                    Log.e("offerList","pricefinal percentage  = "+pricefinal)

                }
                if(offer.type.equals("minus")){
                    if(pricefinal>0 &&pricefinal>price - offer.value) {
                        pricefinal = price - offer.value
                        type_offer ="minus"+"("+ offer.value +")"

                    }
                    if(pricefinal<0){
                        pricefinal = price - offer.value
                        type_offer ="minus"+"("+ offer.value +")"

                    }
                    Log.e("offerList","pricefinal minus  = "+pricefinal)

                }
                if(offer.type.equals("slice")){
                    val num = (price / 100)
                    if(pricefinal>0 &&pricefinal>price - (offer.value*num)){
                        pricefinal = price - (offer.value * num)
                        type_offer ="slice"

                    }
                    if(pricefinal<0){
                        pricefinal = price - (offer.value * num)
                        type_offer ="slice"

                    }
                    Log.e("offerList","pricefinal slice  = "+pricefinal)

                }

            }
            binding.tvTypeOffer.text = type_offer
            binding.tvFinalPrice.text = ""+pricefinal +" €"
            binding.tvInitialPrice.text = ""+price +" €"

        }
        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.loading.observe(this, Observer {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        })
        viewModel.getAllOffers()


    }

}
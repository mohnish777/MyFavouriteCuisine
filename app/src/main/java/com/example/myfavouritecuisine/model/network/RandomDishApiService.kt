package com.example.myfavouritecuisine.model.network

import com.example.myfavouritecuisine.utils.Constants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RandomDishApiService {

    private var retrofit: Retrofit? = null

    fun getInstance(): Retrofit {
        return retrofit ?: synchronized(this) {
            Retrofit.Builder().baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build()
                .also {
                    retrofit = it
                }
        }
    }


    fun getRandomDish(): RandomDishApi {
        return getInstance().create(RandomDishApi::class.java)
    }
}

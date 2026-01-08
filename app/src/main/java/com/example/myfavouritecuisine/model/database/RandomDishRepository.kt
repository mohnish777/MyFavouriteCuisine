package com.example.myfavouritecuisine.model.database

import com.example.myfavouritecuisine.BuildConfig
import com.example.myfavouritecuisine.model.entities.RandomDishResponse
import com.example.myfavouritecuisine.model.network.RandomDishApi
import com.example.myfavouritecuisine.utils.Constants
import io.reactivex.rxjava3.core.Single

class RandomDishRepository(
    private val randomDishApiService: RandomDishApi
) {

    fun getRandomDish(): Single<RandomDishResponse> {
        return randomDishApiService.getRandomDish(
            BuildConfig.API_KEY,
            Constants.INCLUDE_NUTRITION_VALUE,
            Constants.INCLUDE_TAGS_VAL,
            Constants.NUMBER_VAL
        )
    }
}

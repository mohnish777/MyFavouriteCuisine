package com.example.myfavouritecuisine.application

import android.app.Application
import com.example.myfavouritecuisine.model.database.FavDishRepository
import com.example.myfavouritecuisine.model.database.FavDishRoomDatabase
import com.example.myfavouritecuisine.model.database.RandomDishRepository
import com.example.myfavouritecuisine.model.network.RandomDishApiService

class FavDishApplication: Application() {

    private val database by lazy {
        FavDishRoomDatabase.getDatabase(this@FavDishApplication)
    }

    val repository by lazy {
        FavDishRepository(database.FavDishDao())
    }

    val randomDishRepository by lazy {
        RandomDishRepository(RandomDishApiService.getRandomDish())
    }
}

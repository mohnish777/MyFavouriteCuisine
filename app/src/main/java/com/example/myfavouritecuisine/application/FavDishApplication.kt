package com.example.myfavouritecuisine.application

import android.app.Application
import com.example.myfavouritecuisine.model.database.FavDishRepository
import com.example.myfavouritecuisine.model.database.FavDishRoomDatabase

class FavDishApplication: Application() {

    private val database by lazy {
        FavDishRoomDatabase.getDatabase(this@FavDishApplication)
    }

    val repository by lazy {
        FavDishRepository(database.FavDishDao())
    }
}

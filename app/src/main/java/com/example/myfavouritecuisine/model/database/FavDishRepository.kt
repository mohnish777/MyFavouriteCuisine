package com.example.myfavouritecuisine.model.database

import com.example.myfavouritecuisine.model.entities.FavDish

class FavDishRepository(
    private val favDishDao: FavDishDao
) {
    suspend fun insert(favDish: FavDish) {
        favDishDao.insertFavDishData(favDish)
    }
}

package com.example.myfavouritecuisine.model.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.myfavouritecuisine.model.entities.FavDish

interface FavDishDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavDishData(favDish: FavDish)
}

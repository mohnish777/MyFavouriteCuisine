package com.example.myfavouritecuisine.model.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myfavouritecuisine.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavDishData(favDish: FavDish)

    @Query("SELECT * FROM FAV_DISHES_TABLE ORDER BY ID ")
    fun getAllDishList(): Flow<List<FavDish>>
}

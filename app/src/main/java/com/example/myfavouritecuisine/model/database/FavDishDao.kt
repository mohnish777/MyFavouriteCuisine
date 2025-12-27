package com.example.myfavouritecuisine.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.myfavouritecuisine.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavDishData(favDish: FavDish)

    @Query("SELECT * FROM FAV_DISHES_TABLE ORDER BY ID ")
    fun getAllDishList(): Flow<List<FavDish>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateDishes(favDish: FavDish)

    @Query( "SELECT * FROM FAV_DISHES_TABLE WHERE id= :id LIMIT 1")
    fun getFavDishDetailsById(id: Int): Flow<FavDish>

    @Query("SELECT * FROM FAV_DISHES_TABLE WHERE favoriteDish = 1 ORDER BY id")
    fun getFavouriteDishes(): Flow<List<FavDish>>

    @Delete
    suspend fun deleteDish(favDish: FavDish)

    @Query("SELECT * FROM FAV_DISHES_TABLE WHERE type = :type ORDER BY id")
    fun getDishesByType(type: String): Flow<List<FavDish>>
}

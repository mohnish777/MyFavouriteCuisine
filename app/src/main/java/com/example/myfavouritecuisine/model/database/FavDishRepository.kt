package com.example.myfavouritecuisine.model.database

import com.example.myfavouritecuisine.model.entities.FavDish
import com.example.myfavouritecuisine.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch

class FavDishRepository(
    private val favDishDao: FavDishDao
) {
    suspend fun insert(favDish: FavDish) {
        favDishDao.insertFavDishData(favDish)
    }

    private val _allDishListState: MutableStateFlow<Constants.UiState<List<FavDish>>> = MutableStateFlow(Constants.UiState.Idle)
    val allDishListState = _allDishListState.asStateFlow()

    suspend fun loadAllDish()  {
        _allDishListState.value = Constants.UiState.Loading
        try {
            favDishDao.getAllDishList().catch { exception ->
                _allDishListState.value = Constants.UiState.Error(
                    message = exception.message ?:"Error loading dishes",
                    exception = exception
                )

            }.collect { dishes ->
                _allDishListState.value = Constants.UiState.Success(dishes)
            }

        } catch(ex: Exception) {
            _allDishListState.value = Constants.UiState.Error(
                message = ex.message ?: "Error loading dishes",
                exception = ex
            )
        }
    }

    suspend fun loadAllDishes(): Flow<List<FavDish>> {
        return favDishDao.getAllDishList()
    }

}

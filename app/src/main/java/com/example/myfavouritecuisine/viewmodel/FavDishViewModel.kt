package com.example.myfavouritecuisine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfavouritecuisine.model.database.FavDishRepository
import com.example.myfavouritecuisine.model.entities.FavDish
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavDishViewModel(
    private val repository: FavDishRepository
): ViewModel() {

    fun insert(
        favDish: FavDish
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(favDish)
        }
    }
}

package com.example.myfavouritecuisine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myfavouritecuisine.application.FavDishApplication
import com.example.myfavouritecuisine.model.database.FavDishRepository
import com.example.myfavouritecuisine.model.entities.FavDish
import com.example.myfavouritecuisine.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavDishViewModel(
    private val repository: FavDishRepository
): ViewModel() {

    init {
        loadAllDish()
    }

    fun insert(
        favDish: FavDish
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(favDish)
        }
    }

    private val _allDishListState: MutableStateFlow<Constants.UiState<List<FavDish>>> = MutableStateFlow(Constants.UiState.Idle)
    val allDishListState = _allDishListState.asStateFlow()

    fun loadAllDish() {
        viewModelScope.launch(Dispatchers.IO) {
            _allDishListState.value = Constants.UiState.Loading
            try {
                repository.loadAllDishes().catch {
                    _allDishListState.value = Constants.UiState.Error(
                        message = it.message ?: "Error loading the dishes",
                        exception = it
                    )
                }.collect {
                    _allDishListState.value = Constants.UiState.Success(it)
                }
            } catch (ex: Exception) {
                _allDishListState.value = Constants.UiState.Error(
                    message = ex.message ?: "Error loading dishes",
                    exception = ex
                )
            }
        }
    }




    // Define ViewModel factory in a companion object
    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application = checkNotNull(extras[APPLICATION_KEY])
                // Create a SavedStateHandle for this ViewModel from extras
                val savedStateHandle = extras.createSavedStateHandle()

                return FavDishViewModel(
                    (application as FavDishApplication).repository,
                ) as T
            }
        }
    }

}

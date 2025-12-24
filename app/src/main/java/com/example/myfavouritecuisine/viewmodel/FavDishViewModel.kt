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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavDishViewModel(
    private val repository: FavDishRepository
): ViewModel() {

    private var _favouriteDishDetailState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val favouriteDishDetailState: StateFlow<Boolean> = _favouriteDishDetailState

    private var _favouriteDishListState: MutableStateFlow<Constants.UiState<List<FavDish>>> = MutableStateFlow(Constants.UiState.Idle)
    val favouriteDishListState = _favouriteDishListState.asStateFlow()

    private var _currentDishInfo: MutableStateFlow<FavDish>? = null
    val currentDishInfo get() = _currentDishInfo

    private val _allDishListState: MutableStateFlow<Constants.UiState<List<FavDish>>> = MutableStateFlow(Constants.UiState.Idle)
    val allDishListState = _allDishListState.asStateFlow()
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

    fun updateDish(favDish: FavDish) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateDishes(favDish)
        }
    }

    fun observeFavoriteStatus(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getDishDetailsById(id).catch {
                _favouriteDishDetailState.value = false
            }.collect {
                _favouriteDishDetailState.value = it.favoriteDish
                _currentDishInfo?.value = it
            }
        }
    }

    fun loadFavouriteDishes() {
        viewModelScope.launch(Dispatchers.IO) {
            _favouriteDishListState.value = Constants.UiState.Loading
            try {
                repository.getFavouriteDishes().catch {
                    _favouriteDishListState.value = Constants.UiState.Error(
                        message = it.message ?: "Error loading favourite dishes",
                        exception = it
                    )
                }.collect {
                    _favouriteDishListState.value = Constants.UiState.Success(
                        it
                    )
                }
            } catch (ex: Exception) {
                _favouriteDishListState.value = Constants.UiState.Error(
                    message = ex.message ?: "Error loading favourite dishes",
                    exception = ex
                )
            }

        }
    }

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

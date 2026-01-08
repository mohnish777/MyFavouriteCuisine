package com.example.myfavouritecuisine.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.myfavouritecuisine.application.FavDishApplication
import com.example.myfavouritecuisine.model.database.RandomDishRepository
import com.example.myfavouritecuisine.model.entities.RandomDishResponse
import com.example.myfavouritecuisine.utils.Constants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RandomDishViewModel(
    private val randomDishRepository: RandomDishRepository
) : ViewModel() {


    private val compositeDisposable = CompositeDisposable()
    val loadRandomDish: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var _randomDishLoadingError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val randomDishLoadingError = _randomDishLoadingError.asStateFlow()

    private var _randomDishState: MutableStateFlow<Constants.UiState<RandomDishResponse>> = MutableStateFlow(Constants.UiState.Idle)
    val randomDishState = _randomDishState.asStateFlow()

    init{
        Log.d("mohnishUriCheck", "RandomDishViewModel : init ")
    }

    fun getRandomRecipeFromApi() {
        loadRandomDish.value = true
        _randomDishState.value = Constants.UiState.Loading
        val disposable = randomDishRepository
            .getRandomDish()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( { recipes ->
                loadRandomDish.value = false
                _randomDishLoadingError.value = false
                _randomDishState.value = Constants.UiState.Success(recipes)
                Log.d("mohnishUriCheck", "getRandomRecipeFromApi: $recipes ")

            }, { error ->
                _randomDishState.value = Constants.UiState.Error(
                    message = error.message ?: "Error loading random dish",
                    exception = error
                )
                Log.d("mohnishUriCheck", "getRandomRecipeFromApi error: $error ")

            })

        compositeDisposable.add(disposable)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[APPLICATION_KEY])
                return RandomDishViewModel(
                    (application as FavDishApplication).randomDishRepository,
                ) as T
            }
        }
    }
}

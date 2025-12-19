package com.example.myfavouritecuisine.utils

object Constants {

    const val DISH_TYPE: String = "DishType"
    const val DISH_CATEGORY: String = "DishCategory"
    const val DISH_COOKING_TIME: String = "DishCookingTime"

    const val DISH_IMAGE_SOURCE_LOCAL = "Local"
    const val DISH_IMAGE_SOURCE_ONLINE = "Online"

    fun dishType(): ArrayList<String> {
        val list: ArrayList<String> = ArrayList()
        list.add("breakfast")
        list.add("lunch")
        list.add("snacks")
        list.add("dinner")
        list.add("starter")
        list.add("drinks")
        list.add("salad")
        list.add("side dish")
        list.add("desserts")
        list.add("other")
        return list
    }

    fun dishCategory(): ArrayList<String> {
        val list: ArrayList<String> = ArrayList()
        list.add("chinese")
        list.add("italian")
        list.add("japanese")
        list.add("korean")
        list.add("mexican")
        list.add("american")
        list.add("thai")
        list.add("indian")
        list.add("chinese")
        list.add("other")
        return list
    }

    fun dishCookingTime(): ArrayList<String> {
        val list: ArrayList<String> = ArrayList()
        list.add("10")
        list.add("15")
        list.add("20")
        list.add("30")
        list.add("45")
        list.add("50")
        list.add("60")
        list.add("90")
        list.add("120")
        list.add("150")
        list.add("180")
        return list
    }


    sealed class UiState< out T> {
        object Idle: UiState<Nothing>()
        object Loading: UiState<Nothing>()
        data class Success<T>(val data: T): UiState<T>()
        data class Error(val message: String, val exception: Throwable): UiState<Nothing>()

    }
}

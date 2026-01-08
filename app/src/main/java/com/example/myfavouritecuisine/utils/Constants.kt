package com.example.myfavouritecuisine.utils

object Constants {

    const val DISH_TYPE: String = "DishType"
    const val DISH_CATEGORY: String = "DishCategory"
    const val DISH_COOKING_TIME: String = "DishCookingTime"
    const val FILTER_SECTION: String = "FilterSection"
    const val ALL_TYPES: String = "All Types"

    const val DISH_IMAGE_SOURCE_LOCAL = "Local"
    const val DISH_IMAGE_SOURCE_ONLINE = "Online"
    const val EXTRA_DISH_DETAILS = "DishDetails"
    const val API_ENDPOINT: String = "/recipes/random"
    const val INCLUDE_TAGS: String = "include-tags"
    const val NUMBER: String = "number"
    const val API_KEY_VAL: String = "apiKey"
    const val INCLUDE_NUTRITION: String = "includeNutrition"
    ///
    const val INCLUDE_TAGS_VAL: String = "vegetarian,vegan"
    const val NUMBER_VAL: Int = 1
    const val INCLUDE_NUTRITION_VALUE: Boolean = true
    const val BASE_URL: String = "https://api.spoonacular.com"

    const val NOTIFICATION_ID = "FavDish_notification_id"
    const val NOTIFICATION_NAME = "FavDish"
    const val NOTIFICATION_CHANNEL = "FavDish_channel_01"
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

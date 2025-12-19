package com.example.myfavouritecuisine.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_dishes_table")
data class FavDish(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val image: String,
    @ColumnInfo(name = "image_source") val imageSource: String,
    val title: String,
    val type: String,
    val category: String,
    val ingredients: String,
    @ColumnInfo(name = "cooking_time") val cookingTime: String,
    @ColumnInfo("instructions") val directionToCook: String,
    val favoriteDish: Boolean = false

)

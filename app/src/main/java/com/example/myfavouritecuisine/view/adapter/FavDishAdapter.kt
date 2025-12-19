package com.example.myfavouritecuisine.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfavouritecuisine.databinding.ItemDishLayoutBinding
import com.example.myfavouritecuisine.model.entities.FavDish

class FavDishAdapter(
    private val fragment: Fragment
): RecyclerView.Adapter<FavDishAdapter.ViewHolder>() {

    private var dishes: List<FavDish> = emptyList()

    inner class ViewHolder(view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val ivDishImage = view.ivDishImage
        val tvDishTitle = view.tvDishTitle
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavDishAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemDishLayoutBinding = ItemDishLayoutBinding.inflate(
            layoutInflater,
            parent,
            false

        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavDishAdapter.ViewHolder, position: Int) {
        val dish = dishes[position]
        Log.d("mohnishUriCheck", "onBindViewHolder: ${dish.image} ")
        Glide.with(fragment)
            .load(dish.image)
            .into(holder.ivDishImage)
        holder.tvDishTitle.text = dish.title
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun dishList(list: List<FavDish>) {
        dishes = list
        notifyDataSetChanged()
    }
}



package com.example.myfavouritecuisine.view.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myfavouritecuisine.R
import com.example.myfavouritecuisine.databinding.ItemDishLayoutBinding
import com.example.myfavouritecuisine.model.entities.FavDish
import com.example.myfavouritecuisine.utils.Constants
import com.example.myfavouritecuisine.view.activities.AddUpdateDishActivity
import com.example.myfavouritecuisine.view.fragments.AllDishFragment
import com.example.myfavouritecuisine.view.fragments.FavouriteDishesFragment

class FavDishAdapter(
    private val fragment: Fragment
): RecyclerView.Adapter<FavDishAdapter.ViewHolder>() {

    private var dishes: List<FavDish> = emptyList()

    inner class ViewHolder(view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val ivDishImage = view.ivDishImage
        val tvDishTitle = view.tvDishTitle
        val ibMore = view.ibMore
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
        holder.itemView.setOnClickListener {
            when(fragment) {
                is AllDishFragment -> {
                    fragment.dishDetails(dishes[position])
                }
                is FavouriteDishesFragment -> {
                    fragment.dishDetails(dishes[position])
                }
            }
        }

        holder.ibMore.setOnClickListener {
            val popup = PopupMenu(fragment.requireContext(), it)
            val inflater: MenuInflater = popup.menuInflater
            inflater.inflate(R.menu.menu_adapter, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.action_edit_dish -> {
                        val intent = Intent(fragment.requireActivity(), AddUpdateDishActivity::class.java)
                        intent.putExtra(Constants.EXTRA_DISH_DETAILS, dish)
                        fragment.requireActivity().startActivity(intent)
                        true
                    }
                    R.id.delete_dish -> {
                        Log.d("mohnishUriCheck", "onBindViewHolder: delete dish")
                        true
                    }
                    else -> {
                        false
                    }
                }
            }
            popup.show()
        }

        if(fragment is AllDishFragment) {
            holder.ibMore.visibility = View.VISIBLE
        } else if(fragment is FavouriteDishesFragment) {
            holder.ibMore.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    fun dishList(list: List<FavDish>) {
        dishes = list
        notifyDataSetChanged()
    }
}



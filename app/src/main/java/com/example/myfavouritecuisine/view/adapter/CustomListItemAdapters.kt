package com.example.myfavouritecuisine.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfavouritecuisine.databinding.ItemCustomListBinding

class CustomListItemAdapters(
    private val listItems: ArrayList<String>,
    private val selection: String
): RecyclerView.Adapter<CustomListItemAdapters.ViewHolder>() {

    inner class ViewHolder(view: ItemCustomListBinding) : RecyclerView.ViewHolder(view.root) {
        val tvText = view.tvText
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemCustomListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.tvText.text = item
    }

    override fun getItemCount(): Int {
       return listItems.size
    }
}

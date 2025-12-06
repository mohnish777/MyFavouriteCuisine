package com.example.myfavouritecuisine.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfavouritecuisine.databinding.ItemCustomListBinding

class CustomListItemAdapters(
    private val listItems: ArrayList<String>,
    private val selection: String,
    private val listener: onItemClickListener
): RecyclerView.Adapter<CustomListItemAdapters.ViewHolder>() {

    // add listener

    interface onItemClickListener {
        fun onItemOnClick(item: String, selection: String)
    }

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
        holder.itemView.setOnClickListener {
            listener.onItemOnClick(item, selection)
        }
    }

    override fun getItemCount(): Int {
       return listItems.size
    }
}

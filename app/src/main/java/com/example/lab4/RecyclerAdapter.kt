package com.example.lab4

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecyclerAdapter(
    private val onDeleteClick: (DataItem) -> Unit,
    private val categoryDao: CategoryDao // Pass the CategoryDao instance
) : ListAdapter<DataItem, RecyclerAdapter.ItemViewHolder>(DiffCallback()) {

    // ViewHolder
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteItemButton)
        val idTextView: TextView = itemView.findViewById(R.id.idTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.titleTextView.text = item.title
        holder.descriptionTextView.text = item.description


        holder.idTextView.apply {
            text = "Item ID = ${item.id}"
            setTextColor(android.graphics.Color.MAGENTA)
        }

        CoroutineScope(Dispatchers.Main).launch {
            val category = categoryDao.getCategoryById(item.categoryId)
            holder.categoryTextView.apply {
                text = "Category = ${category?.id}"
                setTextColor(android.graphics.Color.RED)
            }
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(item)
        }
    }

    override fun getItemCount(): Int = currentList.size

    class DiffCallback : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
            return oldItem == newItem
        }
    }
}






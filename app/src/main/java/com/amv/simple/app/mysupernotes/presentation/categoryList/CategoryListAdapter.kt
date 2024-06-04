package com.amv.simple.app.mysupernotes.presentation.categoryList

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.CustomPopupMenu
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.data.relations.CategoryAndNote
import com.amv.simple.app.mysupernotes.databinding.ItemCategoryInScreenListBinding
import com.amv.simple.app.mysupernotes.domain.category.CategoryItem
import javax.inject.Inject

class CategoryListAdapter @Inject constructor(
    private val listener: CategoryListListener
): ListAdapter<CategoryAndNote, CategoryListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCategoryInScreenListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        binding.root.setOnClickListener {
            listener.onChooseCategory(it, it.tag as CategoryAndNote)
        }

        binding.ivPopupMenu.setOnClickListener {
            showPopupMenu(it)
        }

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryItem = getItem(position)
        if (categoryItem.category.id == 0){
            holder.binding.ivPopupMenu.visibility = View.GONE
        }
        with(holder.binding) {
            root.tag = categoryItem
            ivPopupMenu.tag = categoryItem
            titleCategory.text = "${categoryItem.category.name} (${categoryItem.notes.size})"
        }
    }

    override fun getItemCount(): Int = currentList.size

    interface CategoryListListener {
        fun onChooseCategory(view: View, categoryItem: CategoryAndNote)
        fun onEditCategory(categoryItem: CategoryAndNote)
        fun onDeleteCategory(categoryItem: CategoryAndNote)
    }
    inner class ViewHolder(val binding: ItemCategoryInScreenListBinding): RecyclerView.ViewHolder(binding.root)

    object DiffCallback : DiffUtil.ItemCallback<CategoryAndNote>() {
        override fun areItemsTheSame(oldItem: CategoryAndNote, newItem: CategoryAndNote): Boolean =
            oldItem.category.id == newItem.category.id

        override fun areContentsTheSame(oldItem: CategoryAndNote, newItem: CategoryAndNote): Boolean =
            oldItem == newItem
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = CustomPopupMenu(view.context, view)
        val categoryItem = view.tag as CategoryAndNote

        // TODO: StringResource 
        popupMenu.menu.add(0,1,Menu.NONE, "Rename").apply {
            setIcon(R.drawable.ic_edit)
        }
        popupMenu.menu.add(0,2,Menu.NONE, "Delete").apply {
            setIcon(R.drawable.ic_delete)
        }

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
               1 -> listener.onEditCategory(categoryItem)
               2 -> listener.onDeleteCategory(categoryItem)
            }
            return@setOnMenuItemClickListener true
        }

        popupMenu.show()

    }
}
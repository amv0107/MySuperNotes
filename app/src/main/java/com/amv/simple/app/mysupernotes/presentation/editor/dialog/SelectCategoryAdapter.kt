package com.amv.simple.app.mysupernotes.presentation.editor.dialog

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amv.simple.app.mysupernotes.databinding.ItemSelectCategoryBinding
import com.amv.simple.app.mysupernotes.domain.category.CategoryItem

class SelectCategoryAdapter(
    private var checkedId: Int,
    private val listener: Listener
) :
    ListAdapter<CategoryItem, SelectCategoryAdapter.ViewHolder>(ItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSelectCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val categoryItem = getItem(position)
        with(holder.binding) {
            if (checkedId == -1)
                ivChecked.visibility = View.INVISIBLE
            else
                ivChecked.visibility = if (checkedId == categoryItem.id)
                    View.VISIBLE
                else
                    View.INVISIBLE

            tvCategoryName.text = categoryItem.name

            root.setOnClickListener {
                ivChecked.visibility = View.VISIBLE
                if (checkedId != categoryItem.id) {
                    notifyItemChanged(checkedId)
                    checkedId = categoryItem.id
                }
                listener.onClick(categoryItem)
            }
        }
    }

    interface Listener {
        fun onClick(categoryItem: CategoryItem)
    }

    inner class ViewHolder(val binding: ItemSelectCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    object ItemCallback : DiffUtil.ItemCallback<CategoryItem>() {
        override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean =
            oldItem == newItem
    }
}
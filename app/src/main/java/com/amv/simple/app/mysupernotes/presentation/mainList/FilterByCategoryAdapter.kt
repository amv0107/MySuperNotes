package com.amv.simple.app.mysupernotes.presentation.mainList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.data.category.CategoryDbModel
import com.amv.simple.app.mysupernotes.databinding.ItemFilterByCategoryBinding
import com.amv.simple.app.mysupernotes.domain.category.CategoryItem
import javax.inject.Inject

class FilterByCategoryAdapter @Inject constructor(
    private val listener: FilterByCategoryListener
) : ListAdapter<CategoryItem, FilterByCategoryAdapter.ViewHolder>(DiffCallback) {

    private var selectedItemPos = -1
    private var lastItemSelectedPos = -1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFilterByCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == selectedItemPos)
            holder.selectedBg()
        else
            holder.defaultBg()

        holder.bindItems(getItem(position))

    }

    interface FilterByCategoryListener {
        fun onClick(categoryItemId: Int?)
    }

    inner class ViewHolder(val binding: ItemFilterByCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                selectedItemPos = adapterPosition
                if (lastItemSelectedPos == -1)
                    lastItemSelectedPos = selectedItemPos
                else {
                    notifyItemChanged(lastItemSelectedPos)
                    lastItemSelectedPos = selectedItemPos
                }
                notifyItemChanged(selectedItemPos)
                listener.onClick(getItem(adapterPosition).id)
            }
        }

        fun bindItems(categoryItem: CategoryItem) {
            binding.tvTitleCategory.text = categoryItem.name
        }

        fun defaultBg() {
            binding.root.background =
                binding.root.context.getDrawable(R.drawable.bg_filter_unselected)
        }

        fun selectedBg() {
            binding.root.background =
                binding.root.context.getDrawable(R.drawable.bg_filter_selected)
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<CategoryItem>() {
        override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: CategoryItem,
            newItem: CategoryItem
        ): Boolean =
            oldItem == newItem
    }


}
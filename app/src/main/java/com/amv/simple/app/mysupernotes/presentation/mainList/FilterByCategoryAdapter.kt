package com.amv.simple.app.mysupernotes.presentation.mainList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.databinding.ItemFilterByCategoryAddCategoryBinding
import com.amv.simple.app.mysupernotes.databinding.ItemFilterByCategoryAllNotesBinding
import com.amv.simple.app.mysupernotes.databinding.ItemFilterByCategoryBinding
import com.amv.simple.app.mysupernotes.domain.category.CategoryItem
import javax.inject.Inject

/**
 * Header and Footer from: https://medium.com/@youssef7agar/multiple-viewholder-listadapter-part-1-the-creation-b9f2380e8820
 */
class FilterByCategoryAdapter @Inject constructor(
    selectedItem: Int = 0,
    private val listener: FilterByCategoryListener
) : ListAdapter<CategoryItem, RecyclerView.ViewHolder>(DiffCallback) {

    private var selectedItemPos = selectedItem
    private var lastItemSelectedPos = selectedItem

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ALL_NOTES_VIEW -> {
                AllNotesViewHolder(
                    ItemFilterByCategoryAllNotesBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            CATEGORY_VIEW -> {
                CategoryViewHolder(
                    ItemFilterByCategoryBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            ADD_NEW_CATEGORY -> {
                AddNewCategoryViewHolder(
                    ItemFilterByCategoryAddCategoryBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }

            else -> throw IllegalArgumentException("Invalid type of view $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            ALL_NOTES_VIEW
        else if (position > currentList.lastIndex + 1)
            ADD_NEW_CATEGORY
        else
            CATEGORY_VIEW
    }

    override fun getItemCount(): Int {
        return currentList.size + 2
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is AllNotesViewHolder ->
                if (position == selectedItemPos) holder.selectedBg() else holder.defaultBg()

            is CategoryViewHolder -> {
                if (position == selectedItemPos) holder.selectedBg() else holder.defaultBg()
                holder.bindItems(getItem(position - 1))
            }

            is AddNewCategoryViewHolder ->
                if (position == selectedItemPos) holder.selectedBg() else holder.defaultBg()
        }
    }

    interface FilterByCategoryListener {
        fun onClickItem(categoryItemId: Int?)
        fun onClickAll()
        fun onClickAdd()
    }

    inner class CategoryViewHolder(val binding: ItemFilterByCategoryBinding) :
        BaseViewHolder(binding.root) {

        override fun onClick() {
            listener.onClickItem(getItem(adapterPosition - 1).id)
        }

        fun bindItems(categoryItem: CategoryItem) {
            binding.tvTitleCategory.text = categoryItem.name
        }

    }

    inner class AllNotesViewHolder(val binding: ItemFilterByCategoryAllNotesBinding) :
        BaseViewHolder(binding.root) {
        override fun onClick() {
            listener.onClickAll()
        }
    }

    inner class AddNewCategoryViewHolder(val binding: ItemFilterByCategoryAddCategoryBinding) :
        BaseViewHolder(binding.root) {
        override fun onClick() {
            listener.onClickAdd()
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

    companion object {
        private const val ALL_NOTES_VIEW = 0
        private const val CATEGORY_VIEW = 1
        private const val ADD_NEW_CATEGORY = 2
    }

    abstract inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun onClick()

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
                onClick()
            }
        }

        fun defaultBg() {
            itemView.background = itemView.context.getDrawable(R.drawable.bg_filter_unselected)
        }

        fun selectedBg() {
            itemView.background = itemView.context.getDrawable(R.drawable.bg_filter_selected)
        }
    }
}

package com.amv.simple.app.mysupernotes.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amv.simple.app.mysupernotes.databinding.ItemNoteListBinding
import com.amv.simple.app.mysupernotes.domain.NoteItem


/**
 * https://www.youtube.com/watch?v=pxOybZErY3w&list=PLRmiL0mct8WnntEXpHpP9S5ewtjgWook_&index=8&t=646s
 */
class MainListAdapter(
    private val listener: MainListListener
) : ListAdapter<NoteItem, MainListAdapter.MainViewHolder>(ItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = ItemNoteListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        binding.root.setOnClickListener {
            listener.onChooseNote(it.tag as NoteItem)
        }
        binding.root.setOnLongClickListener {
            listener.onItemAction(it.tag as NoteItem)
            true
        }

        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val noteItem = getItem(position)
        with(holder.binding) {
            root.tag = noteItem
            tvTitleNote.text = noteItem.title
            tvDateTimeNote.text = noteItem.date
            tvTextContentNote.text = noteItem.textContent

//            tvTitleNote.backgroundTintList = ColorStateList.valueOf(
//                ContextCompat.getColor(root.context, tintColor)
//            )
//            pin.setImageResource(
//                if (noteItem.isPin)
//                    R.drawable.ic_pin
//                else
//                    R.drawable.ic_un_pin
//            )
        }
    }

    override fun getItemCount(): Int = currentList.size

    interface MainListListener {
        fun onChooseNote(noteItem: NoteItem)
        fun onItemAction(noteItem: NoteItem)
    }

    inner class MainViewHolder(val binding: ItemNoteListBinding) : RecyclerView.ViewHolder(binding.root)

    object ItemCallback : DiffUtil.ItemCallback<NoteItem>() {
        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean =
            oldItem == newItem
    }
}
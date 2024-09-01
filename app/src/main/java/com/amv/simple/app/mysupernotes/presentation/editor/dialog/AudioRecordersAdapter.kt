package com.amv.simple.app.mysupernotes.presentation.editor.dialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amv.simple.app.mysupernotes.data.note.Attachment
import com.amv.simple.app.mysupernotes.databinding.ItemAudioRecorderBinding

class AudioRecordersAdapter(
   private val listener: OnPlayAudioRecorder
) : ListAdapter<Attachment, AudioRecordersAdapter.ViewHolder>(ItemCallback) {

    interface OnPlayAudioRecorder {
        fun onClickItemAudioRecorder(item: Attachment)
    }

    inner class ViewHolder(val binding: ItemAudioRecorderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemAudioRecorderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.binding.ivPlay.setOnClickListener {
            listener.onClickItemAudioRecorder(item)
        }
    }

    object ItemCallback : DiffUtil.ItemCallback<Attachment>() {
        override fun areItemsTheSame(oldItem: Attachment, newItem: Attachment): Boolean =
            oldItem.fileName == newItem.fileName

        override fun areContentsTheSame(oldItem: Attachment, newItem: Attachment): Boolean =
            oldItem == newItem
    }
}
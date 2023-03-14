package com.arch.template.ui.feature.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arch.presentation.model.NotePresentation
import com.arch.template.R
import com.arch.template.databinding.ItemNoteListingBinding

class NoteListingAdapter : ListAdapter<NotePresentation, NoteListingAdapter.ViewHolder>(DiffUtilCallBack) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemNoteListingBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_note_listing,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: ItemNoteListingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotePresentation) {
            with(binding) {
                noteData = item
            }
        }
    }

    object DiffUtilCallBack : DiffUtil.ItemCallback<NotePresentation>() {
        override fun areItemsTheSame(
            oldItem: NotePresentation,
            newItem: NotePresentation
        ): Boolean {
            return oldItem.noteId == newItem.noteId
        }

        override fun areContentsTheSame(
            oldItem: NotePresentation,
            newItem: NotePresentation
        ): Boolean {
            return oldItem == newItem
        }
    }
}
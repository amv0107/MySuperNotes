package com.amv.simple.app.mysupernotes.domain

data class NoteItem(
    val id: Int = UNDEFINED_ID,
    val title: String,
    val textContent: String,
    val date: String,
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val isArchive: Boolean = false,
    val isDelete: Boolean = false,
    val deleteDate: String = ""
) {
    companion object{
        const val UNDEFINED_ID = 0
    }
}


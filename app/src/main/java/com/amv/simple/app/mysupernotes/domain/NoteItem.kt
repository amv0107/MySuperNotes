package com.amv.simple.app.mysupernotes.domain

data class NoteItem(
    val id: Int = UNDEFINED_ID,
    val title: String,
    val textContent: String,
    val date: String,
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
) {
    companion object{
        const val UNDEFINED_ID = 0
    }
}


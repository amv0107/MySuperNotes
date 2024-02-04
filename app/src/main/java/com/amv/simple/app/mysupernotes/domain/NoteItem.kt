package com.amv.simple.app.mysupernotes.domain

data class NoteItem(
    val id: Int = UNDEFINED_ID,
    val title: String,
    val textContent: String,
    val date: String,
) {
    companion object{
        const val UNDEFINED_ID = 0
    }
}


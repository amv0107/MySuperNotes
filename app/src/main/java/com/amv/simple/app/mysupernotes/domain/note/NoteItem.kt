package com.amv.simple.app.mysupernotes.domain.note

data class NoteItem(
    val id: Int = UNDEFINED_ID,
    val title: String,
    val textContent: String,
    val dateOfCreate: Long,
    val isPinned: Boolean = false,
    val isFavorite: Boolean = false,
    val isArchive: Boolean = false,
    val isDelete: Boolean = false,
    val deletionDate: Long = 0,
    val categoryId: Int = WITHOUT_CATEGORY,
) {
    companion object {
        const val UNDEFINED_ID = 0
        const val WITHOUT_CATEGORY = 0
    }
}


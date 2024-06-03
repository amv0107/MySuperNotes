package com.amv.simple.app.mysupernotes.domain.category

data class CategoryItem(
    val id: Int = UNDEFINED_ID,
    val position: Int,
    val name: String,
) {
    companion object{
        const val UNDEFINED_ID = 0
    }
}
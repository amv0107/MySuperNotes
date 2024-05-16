package com.amv.simple.app.mysupernotes.data.category

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "category_items"
)
data class CategoryDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val position: Int,
    val name: String,

)

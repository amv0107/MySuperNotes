package com.amv.simple.app.mysupernotes.data.whatsNew

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "whats_new_items"
)
data class WhatsNewDbModel(
    @PrimaryKey(autoGenerate = false)
    val versionName: String,
    val description: String,
)

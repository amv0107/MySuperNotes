package com.amv.simple.app.mysupernotes.data.whatsNew

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WhatsNewDao {

    @Query("SELECT * FROM whats_new_items")
    fun getWhatsNewList(): Flow<List<WhatsNewDbModel>>
}
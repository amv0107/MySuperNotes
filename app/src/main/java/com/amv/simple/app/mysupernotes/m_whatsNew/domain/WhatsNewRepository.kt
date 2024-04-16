package com.amv.simple.app.mysupernotes.m_whatsNew.domain

import kotlinx.coroutines.flow.Flow

interface WhatsNewRepository {

    fun getWhatsNewList(): Flow<List<WhatsNewItem>>
}
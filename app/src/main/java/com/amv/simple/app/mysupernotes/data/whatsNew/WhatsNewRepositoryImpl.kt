package com.amv.simple.app.mysupernotes.data.whatsNew

import com.amv.simple.app.mysupernotes.m_whatsNew.domain.WhatsNewItem
import com.amv.simple.app.mysupernotes.m_whatsNew.domain.WhatsNewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WhatsNewRepositoryImpl @Inject constructor(
    private val dao: WhatsNewDao,
    private val mapper: WhatsNewMapper
): WhatsNewRepository {

    override fun getWhatsNewList(): Flow<List<WhatsNewItem>> =
        dao.getWhatsNewList().map {
            mapper.mapListDbModelToListModel(it)
        }
}
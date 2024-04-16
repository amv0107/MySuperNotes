package com.amv.simple.app.mysupernotes.data.whatsNew

import com.amv.simple.app.mysupernotes.m_whatsNew.domain.WhatsNewItem

class WhatsNewMapper {

    fun mapEntityToDbModel(whatsNewItem: WhatsNewItem) = WhatsNewDbModel(
        versionName = whatsNewItem.versionName,
        description = whatsNewItem.description
    )

    fun mapDbModelToEntity(whatsNewDbModel: WhatsNewDbModel) = WhatsNewItem(
        versionName = whatsNewDbModel.versionName,
        description = whatsNewDbModel.description
    )

    fun mapListDbModelToListModel(list: List<WhatsNewDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}
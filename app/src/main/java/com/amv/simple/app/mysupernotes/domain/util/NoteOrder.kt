package com.amv.simple.app.mysupernotes.domain.util

import java.io.Serializable

sealed class NoteOrder(val orderType: OrderType): Serializable {
    class Title(orderType: OrderType) : NoteOrder(orderType)
    class DateCreate(orderType: OrderType) : NoteOrder(orderType)
//    class DateLastEdit(orderType: OrderType): NoteOrder(orderType)

    fun copy(orderType: OrderType): NoteOrder {
        return when (this) {
            is Title -> Title(orderType)
            is DateCreate -> DateCreate(orderType)
        }
    }


}

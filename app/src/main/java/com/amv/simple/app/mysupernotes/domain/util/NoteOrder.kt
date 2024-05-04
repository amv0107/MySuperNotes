package com.amv.simple.app.mysupernotes.domain.util

sealed class NoteOrder(val orderType: OrderType) {
    class Title(orderType: OrderType): NoteOrder(orderType)
    class DateCreate(orderType: OrderType): NoteOrder(orderType)
//    class DateLastEdit(orderType: OrderType): NoteOrder(orderType)
}

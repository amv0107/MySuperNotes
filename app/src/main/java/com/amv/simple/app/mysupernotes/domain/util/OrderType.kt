package com.amv.simple.app.mysupernotes.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}

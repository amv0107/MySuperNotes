package com.amv.simple.app.mysupernotes.domain.util

import java.io.Serializable

sealed class OrderType: Serializable {
    object Ascending: OrderType()
    object Descending: OrderType()
}

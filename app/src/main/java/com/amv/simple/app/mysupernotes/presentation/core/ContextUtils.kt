package com.amv.simple.app.mysupernotes.presentation.core

import android.content.Context
import android.util.TypedValue

fun Context.resolveAttribute(resId: Int): Int? {
    val typeValue = TypedValue()
    return if (theme.resolveAttribute(resId, typeValue, true)) typeValue.data else null
}
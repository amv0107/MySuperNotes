package com.amv.simple.app.mysupernotes.presentation.editor

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object TimeManager {

    fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("dd/MM/yyyy - HH:mm", Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }
}
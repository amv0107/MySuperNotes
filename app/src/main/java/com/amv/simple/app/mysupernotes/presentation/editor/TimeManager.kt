package com.amv.simple.app.mysupernotes.presentation.editor

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object TimeManager {

    private const val DEF_TIME_FORMAT = "dd/MM/yyyy - HH:mm"

    fun getCurrentTime(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern(DEF_TIME_FORMAT, Locale.getDefault())
        return current.format(formatter)
    }

    fun getTimeFormat(time: String, newPatternDateTime: String): String {
        val defFormatter = DateTimeFormatter.ofPattern(DEF_TIME_FORMAT, Locale.getDefault())
        val defDate = defFormatter.parse(time)
        val newFormatter = DateTimeFormatter.ofPattern(newPatternDateTime, Locale.getDefault())
        return newFormatter.format(defDate) ?: time
    }
}
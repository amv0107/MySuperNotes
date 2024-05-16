package com.amv.simple.app.mysupernotes.presentation.editor

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object TimeManager {

    fun getCurrentTimeToDB(): Long {
        return System.currentTimeMillis()
    }

    fun getTimeToDisplay(time: Long, newPatternDateTime: String): String {
        val date = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(time),
            ZoneId.systemDefault()
        )
        return DateTimeFormatter.ofPattern(newPatternDateTime, Locale.getDefault())
            .format(date)
    }
}
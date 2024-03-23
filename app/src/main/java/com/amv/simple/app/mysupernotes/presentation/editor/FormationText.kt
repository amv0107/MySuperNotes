package com.amv.simple.app.mysupernotes.presentation.editor

import android.graphics.Typeface
import android.text.Spannable
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.EditText

object FormationText {

    fun bold(startPos: Int, endPos: Int, view: EditText){
        val styles = view.text.getSpans(startPos, endPos, StyleSpan::class.java)
        var boldStyle: StyleSpan? = null
        if (styles.isNotEmpty()) {
            view.text.removeSpan(styles[0])
        } else {
            boldStyle = StyleSpan(Typeface.BOLD)
        }
        view.text.setSpan(boldStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.text.trim()
    }

    fun italic(startPos: Int, endPos: Int, view: EditText){
        val styles = view.text.getSpans(startPos, endPos, StyleSpan::class.java)
        var italicStyle: StyleSpan? = null
        if (styles.isNotEmpty()) {
            view.text.removeSpan(styles[0])
        } else {
            italicStyle = StyleSpan(Typeface.ITALIC)
        }
        view.text.setSpan(italicStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.text.trim()
    }

    fun underline(startPos: Int, endPos: Int, view: EditText){
        val styles = view.text.getSpans(startPos, endPos, UnderlineSpan::class.java)
        var underlineStyle: UnderlineSpan? = null
        if (styles.isNotEmpty()) {
            view.text.removeSpan(styles[0])
        } else {
            underlineStyle = UnderlineSpan()
        }
        view.text.setSpan(underlineStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.text.trim()
    }

    fun foregroundColorText(startPos: Int, endPos: Int, view: EditText) {

    }

    fun backgroundColorText(startPos: Int, endPos: Int, view: EditText) {

    }

    fun sizeTextDecrease(startPos: Int, endPos: Int, view: EditText) {
        // Уменьшаем значение в поле на 2 позиции
        // Применяем новое значение для выбранного текста
    }

    fun sizeTextIncrease(startPos: Int, endPos: Int, view: EditText) {
        // Увеличиваем значение в поле на 2 позиции
        // Применяем новое значение для выбранного текста
    }

    private fun formationSize(startPos: Int, endPos: Int, view: EditText, size: Int) {
        // Применяем новое значение для выбранного текста
    }
}
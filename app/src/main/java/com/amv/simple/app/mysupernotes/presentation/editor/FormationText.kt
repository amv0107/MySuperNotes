package com.amv.simple.app.mysupernotes.presentation.editor

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.style.AbsoluteSizeSpan
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.widget.EditText

object FormationText {

    fun bold(startPos: Int, endPos: Int, view: EditText) {
        val styles = view.text.getSpans(startPos, endPos, StyleSpan::class.java)
            .filter { it.style == Typeface.BOLD }
        var boldStyle: StyleSpan? = null
        if (styles.isNotEmpty()) {
            view.text.removeSpan(styles[0])
        } else {
            boldStyle = StyleSpan(Typeface.BOLD)
        }
        view.text.setSpan(boldStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.text.trim()
    }

    fun italic(startPos: Int, endPos: Int, view: EditText) {
        val styles = view.text.getSpans(startPos, endPos, StyleSpan::class.java)
            .filter { it.style == Typeface.ITALIC }
        var italicStyle: StyleSpan? = null
        if (styles.isNotEmpty()) {
            view.text.removeSpan(styles[0])
        } else {
            italicStyle = StyleSpan(Typeface.ITALIC)
        }
        view.text.setSpan(italicStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.text.trim()
    }

    fun underline(startPos: Int, endPos: Int, view: EditText) {
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

    fun getForegroundColorText(startPos: Int, endPos: Int, view: EditText): String {
        val style = view.text.getSpans(startPos, endPos, ForegroundColorSpan::class.java)
        return if (style.isNotEmpty()) "#" + Integer.toHexString(style[0].foregroundColor)
            .uppercase() else "#FF262626"
    }

    fun getBackgroundColorText(startPos: Int, endPos: Int, view: EditText): String {
        val style = view.text.getSpans(startPos, endPos, BackgroundColorSpan::class.java)
        return if (style.isNotEmpty()) "#" + Integer.toHexString(style[0].backgroundColor)
            .uppercase() else "#FFFFFFFF"
    }

    fun foregroundColorText(startPos: Int, endPos: Int, color: Int, view: EditText) {
        val styles = view.text.getSpans(startPos, endPos, ForegroundColorSpan::class.java)
        if (styles.isNotEmpty())
            view.text.removeSpan(styles[0])

        view.text.setSpan(
            ForegroundColorSpan(color),
            startPos,
            endPos,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        view.text.trim()
    }

    fun backgroundColorText(startPos: Int, endPos: Int, color: Int, view: EditText) {
        // TODO: Если цвет белый то просто удалять стиль НЕ присваивая новый
        val styles = view.text.getSpans(startPos, endPos, BackgroundColorSpan::class.java)
        if (styles.isNotEmpty())
            view.text.removeSpan(styles[0])
        if (color != Color.parseColor("#FFFFFFFF"))
            view.text.setSpan(
                BackgroundColorSpan(color),
                startPos,
                endPos,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

        view.text.trim()
    }

    fun sizeText(startPos: Int, endPos: Int, size: Int, view: EditText) {
        val styles = view.text.getSpans(startPos, endPos, AbsoluteSizeSpan::class.java)
        if (styles.isNotEmpty()) view.text.removeSpan(styles[0])
        view.text.setSpan(
            AbsoluteSizeSpan(size, true),
            startPos,
            endPos,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        view.text.trim()
    }

}
package com.amv.simple.app.mysupernotes.presentation.editor

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
        val styles = view.text.getSpans(startPos, endPos, StyleSpan::class.java).filter { it.style == Typeface.BOLD }
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
        val styles = view.text.getSpans(startPos, endPos, StyleSpan::class.java).filter { it.style == Typeface.ITALIC }
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

    fun foregroundColorText(startPos: Int, endPos: Int, color: Int, view: EditText) {
        val styles = view.text.getSpans(startPos, endPos, ForegroundColorSpan::class.java)
        var foregroundColorStyle: ForegroundColorSpan? = null
        if (styles.isNotEmpty()) {
            view.text.removeSpan(styles[0])
        } else {
            foregroundColorStyle = ForegroundColorSpan(color)
        }
        view.text.setSpan(foregroundColorStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.text.trim()
    }

    fun backgroundColorText(startPos: Int, endPos: Int, color: Int, view: EditText) {
        val styles = view.text.getSpans(startPos, endPos, BackgroundColorSpan::class.java)
        var backgroundColorStyle: BackgroundColorSpan? = null
        if (styles.isNotEmpty()) {
            view.text.removeSpan(styles[0])
        } else {
            backgroundColorStyle = BackgroundColorSpan(color)
        }
        view.text.setSpan(backgroundColorStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.text.trim()
    }

    fun sizeText(startPos: Int, endPos: Int, size: Int, view: EditText) {
        view.text.setSpan(AbsoluteSizeSpan(size, true), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        view.text.trim()
    }
}
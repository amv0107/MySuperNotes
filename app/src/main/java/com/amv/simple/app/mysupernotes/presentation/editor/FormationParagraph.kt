package com.amv.simple.app.mysupernotes.presentation.editor

import android.text.Layout
import android.text.style.AlignmentSpan
import android.text.style.BulletSpan
import android.widget.EditText

object FormationParagraph {

    fun alignLeft(startPos: Int, endPos: Int, view: EditText) {
        val styles = view.text.getSpans(startPos, endPos, AlignmentSpan::class.java)

        var leftStyle: AlignmentSpan? = null
        if (styles.isNotEmpty() && (styles[0].alignment == Layout.Alignment.ALIGN_CENTER || styles[0].alignment == Layout.Alignment.ALIGN_OPPOSITE)) {
            view.text.removeSpan(styles[0])
            leftStyle = AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL)
        } else if (styles.isNotEmpty() && styles[0].alignment == Layout.Alignment.ALIGN_NORMAL)
            view.text.removeSpan(styles[0])
        else
            leftStyle = AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL)

        val paragraph = view.text.toString().substring(0, endPos)
        val index = if (paragraph.contains("\n"))
            paragraph.lastIndexOf("\n") + 1
        else
            0
        view.text.setSpan(leftStyle, index, endPos, 0)
    }

    fun alignCenter(startPos: Int, endPos: Int, view: EditText) {
        val styles = view.text.getSpans(startPos, endPos, AlignmentSpan::class.java)

        var centerStyle: AlignmentSpan? = null
        if (styles.isNotEmpty() && (styles[0].alignment == Layout.Alignment.ALIGN_NORMAL || styles[0].alignment == Layout.Alignment.ALIGN_OPPOSITE)) {
            view.text.removeSpan(styles[0])
            centerStyle = AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER)
        } else if (styles.isNotEmpty() && styles[0].alignment == Layout.Alignment.ALIGN_CENTER)
            view.text.removeSpan(styles[0])
        else
            centerStyle = AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER)

        val paragraph = view.text.toString().substring(0, endPos)
        val index = if (paragraph.contains("\n"))
            paragraph.lastIndexOf("\n") + 1
        else
            0
        view.text.setSpan(centerStyle, index, endPos, 0)
    }

    fun alignRight(startPos: Int, endPos: Int, view: EditText) {
        val styles = view.text.getSpans(startPos, endPos, AlignmentSpan::class.java)

        var rightStyle: AlignmentSpan? = null
        if (styles.isNotEmpty() && (styles[0].alignment == Layout.Alignment.ALIGN_NORMAL || styles[0].alignment == Layout.Alignment.ALIGN_CENTER)) {
            view.text.removeSpan(styles[0])
            rightStyle = AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE)
        } else if (styles.isNotEmpty() && styles[0].alignment == Layout.Alignment.ALIGN_OPPOSITE)
            view.text.removeSpan(styles[0])
        else
            rightStyle = AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE)

        val paragraph = view.text.toString().substring(0, endPos)
        val index = if (paragraph.contains("\n"))
            paragraph.lastIndexOf("\n") + 1
        else
            0
        view.text.setSpan(rightStyle, index, endPos, 0)
    }

    fun bulletSpan(startPos: Int, endPos: Int, view: EditText, color: Int) {
        val styles = view.text.getSpans(startPos, endPos, BulletSpan::class.java)
        var bulletStyle: BulletSpan? = null
        if (styles.isNotEmpty()) {
            view.text.removeSpan(styles[0])
        } else
            bulletStyle = BulletSpan(20, color, 10)


        val paragraph = view.text.toString().substring(0, startPos)
        val index = if (paragraph.contains("\n"))
            paragraph.lastIndexOf("\n") + 1
        else
            0

        view.text.setSpan(bulletStyle, index, endPos, 0)
    }
}
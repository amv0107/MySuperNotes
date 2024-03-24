package com.amv.simple.app.mysupernotes.presentation.editor

import android.text.Layout
import android.text.style.AlignmentSpan
import android.text.style.BulletSpan
import android.widget.EditText

object FormationParagraph {

    fun alignLeft(startPos: Int, endPos: Int, view: EditText) {
        val paragraph = view.text.toString().substring(0, startPos)
        val index = if (paragraph.contains("\n"))
            paragraph.lastIndexOf("\n") + 1
        else
            0
        view.text.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL),
            index,
            endPos,
            0
        )
    }

    fun alignCenter(startPos: Int, endPos: Int, view: EditText) {
        val paragraph = view.text.toString().substring(0, startPos)
        val index = if (paragraph.contains("\n"))
            paragraph.lastIndexOf("\n") + 1
        else
            0
        view.text.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
            index,
            endPos,
            0
        )
    }

    fun alignRight(startPos: Int, endPos: Int, view: EditText) {
        val paragraph = view.text.toString().substring(0, startPos)
        val index = if (paragraph.contains("\n"))
            paragraph.lastIndexOf("\n") + 1
        else
            0
        view.text.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE),
            index,
            endPos,
            0
        )
    }

    fun bulletSpan(startPos: Int, endPos: Int, view: EditText, color: Int) {
        val styles = view.text.getSpans(startPos, endPos, BulletSpan::class.java)
        var bulletStyle: BulletSpan? = null
        if (styles.isNotEmpty()) {
            view.text.removeSpan(styles[0])
        } else {
            bulletStyle = BulletSpan(20, color, 10)
        }

        val paragraph = view.text.toString().substring(0, startPos)
        val index = if (paragraph.contains("\n"))
            paragraph.lastIndexOf("\n") + 1
        else
            0

        view.text.setSpan(
            bulletStyle,
            index,
            endPos,
            0
        )
    }
}
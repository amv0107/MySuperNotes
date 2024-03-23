package com.amv.simple.app.mysupernotes.presentation.editor

import android.text.Layout
import android.text.Spannable
import android.text.style.AlignmentSpan
import android.widget.EditText

object FormationParagraph {

    fun alignLeft(startPos: Int, endPos: Int, view: EditText) {
        view.text.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL),
            startPos,
            endPos,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    fun alignCenter(startPos: Int, endPos: Int, view: EditText) {
        view.text.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),
            startPos,
            endPos,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    fun alignRight(startPos: Int, endPos: Int, view: EditText) {
        view.text.setSpan(
            AlignmentSpan.Standard(Layout.Alignment.ALIGN_OPPOSITE),
            startPos,
            endPos,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    fun bulletSpan(startPos: Int, endPos: Int, view: EditText) {

    }
}
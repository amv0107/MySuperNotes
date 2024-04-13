package com.amv.simple.app.mysupernotes.domain.util

import android.content.Context
import android.content.Intent
import android.text.Html
import com.amv.simple.app.mysupernotes.R
import com.amv.simple.app.mysupernotes.domain.NoteItem

object ShareHelper {

    fun shareTextNoteItem(noteItem: NoteItem): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.apply {
            putExtra(Intent.EXTRA_TEXT, makeShareText(noteItem))
        }
        return intent
    }

    private fun makeShareText(noteItem: NoteItem): String {
        val sBuilder = StringBuilder()
        sBuilder.append("--- ${noteItem.title} ---")
        sBuilder.append("\n\n")
        sBuilder.append(Html.fromHtml(noteItem.textContent, Html.FROM_HTML_MODE_COMPACT))

        return sBuilder.toString()
    }

    fun shareApp(context: Context): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.apply {
            putExtra(Intent.EXTRA_TEXT, makeShareAppText(context))
        }
        return intent
    }

    private fun makeShareAppText(context: Context): String {
        val sBuilder = StringBuilder()
        sBuilder.append(context.getString(R.string.share_app_message))
        sBuilder.append("\n\n")
        sBuilder.append(context.getString(R.string.share_app_link))

        return sBuilder.toString()
    }
}
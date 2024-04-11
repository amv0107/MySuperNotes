package com.amv.simple.app.mysupernotes.presentation.editor

import android.widget.EditText
import androidx.core.content.ContextCompat
import com.amv.simple.app.mysupernotes.R
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_SLIDE
import com.google.android.material.snackbar.Snackbar

typealias ListenerReDelete = () -> Unit

fun EditText.setReadOnly(value: Boolean, listenerReDelete: ListenerReDelete) {
    isFocusableInTouchMode = !value

    setOnClickListener {
        if (value)
            Snackbar.make(it, resources.getString(R.string.readOnly), Snackbar.LENGTH_SHORT)
                .setBackgroundTint(ContextCompat.getColor(context, R.color.grey))
                .setTextColor(ContextCompat.getColor(context, R.color.black))
                .setActionTextColor(ContextCompat.getColor(context, R.color.orange))
                .setAnimationMode(ANIMATION_MODE_SLIDE)
                .setAction(resources.getString(R.string.action_regain_access)) {
                    listenerReDelete()
                }.show()
    }
}
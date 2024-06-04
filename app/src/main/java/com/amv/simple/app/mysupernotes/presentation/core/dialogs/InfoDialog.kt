package com.amv.simple.app.mysupernotes.presentation.core.dialogs

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.amv.simple.app.mysupernotes.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Fragment.infoDialog(@StringRes message: Int) {

    val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.ConfirmDialogFragment)
        .setIcon(R.drawable.ic_info)
        .setTitle(R.string.title_info_dialog)
        .setMessage(message)
        .setPositiveButton(R.string.positive_button_info_dialog, null)
        .setCancelable(true)
        .create()

    dialog.show()
}

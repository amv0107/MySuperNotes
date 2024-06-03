package com.amv.simple.app.mysupernotes.presentation.core

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Fragment.showToast(@StringRes message: Int) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
package com.amv.simple.app.mysupernotes.presentation.core

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

fun Fragment.showToast(@StringRes message: Int) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

fun Fragment.showToast(message: String) =
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

fun ViewModel.parseText(inputText: String?): String = inputText?.trim() ?: ""
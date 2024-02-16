package com.amv.simple.app.mysupernotes.presentation.core

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

interface ChangeToolBar {
    fun updateTitleToolbar(@StringRes title: Int)
    fun updateColorToolbar(@ColorRes backgroundColor: Int)
    fun updateStatusBarColor(@ColorRes backgroundColor: Int)
}
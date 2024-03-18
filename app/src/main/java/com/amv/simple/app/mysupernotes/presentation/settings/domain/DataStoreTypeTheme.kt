package com.amv.simple.app.mysupernotes.presentation.settings.domain

import androidx.annotation.StringRes
import com.amv.simple.app.mysupernotes.R

enum class DataStoreTypeTheme(@StringRes val title: Int) {
    LIGHT(R.string.light_theme),
    DARK(R.string.dark_theme),
    SYSTEM(R.string.system_theme);
}
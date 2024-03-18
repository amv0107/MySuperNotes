package com.amv.simple.app.mysupernotes.presentation.settings.domain

import androidx.annotation.StringRes
import com.amv.simple.app.mysupernotes.R

enum class DataStoreStyleListNotes(@StringRes val title: Int) {
    LIST(R.string.list_menu_linear),
    GRID(R.string.list_menu_staggered_grid),
}
package com.amv.simple.app.mysupernotes.presentation.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amv.simple.app.mysupernotes.data.PreferencesManager
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreFormatDateTime
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreLanguage
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreStyleListNotes
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreTypeTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
) : ViewModel() {

    val languageFlow = preferencesManager.languageFlow
    val formatDateTimeFlow = preferencesManager.formatDataTimeFlow
    val themeAppFlow = preferencesManager.themeFlow
    val styleListNotesFlow = preferencesManager.layoutManagerFlow

    fun onTypeLanguageApp(dataStoreLanguage: DataStoreLanguage) = viewModelScope.launch {
        preferencesManager.updateTypeLanguageApp(dataStoreLanguage)
    }

    fun onTypeFormatDateTime(dataStoreFormatDateTime: DataStoreFormatDateTime) = viewModelScope.launch {
        preferencesManager.updateTypeFormatDateTime(dataStoreFormatDateTime)
    }

    fun onTheme(dataStoreTypeTheme: DataStoreTypeTheme) = viewModelScope.launch {
        preferencesManager.updateThemeApp(dataStoreTypeTheme)
    }

    fun onStyleListNotes(dataStoreStyleListNotes: DataStoreStyleListNotes) = viewModelScope.launch {
        preferencesManager.updateTypeLayoutManager(dataStoreStyleListNotes)
    }
}
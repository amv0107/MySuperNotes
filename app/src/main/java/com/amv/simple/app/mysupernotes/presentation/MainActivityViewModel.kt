package com.amv.simple.app.mysupernotes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.amv.simple.app.mysupernotes.data.PreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    val themeAppFlow = preferencesManager.themeFlow.asLiveData()
}
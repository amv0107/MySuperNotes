package com.amv.simple.app.mysupernotes.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreFormatDateTime
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreLanguage
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreStyleListNotes
import com.amv.simple.app.mysupernotes.presentation.settings.domain.DataStoreTypeTheme
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PreferencesManager"

data class LayoutManagerPreferences(val dataStoreStyleListNotes: DataStoreStyleListNotes)
data class LanguageAppPreferences(val languageApp: DataStoreLanguage)
data class FormatDateTimePreferences(val formatDataTime: DataStoreFormatDateTime)
data class ThemeAppPreferences(val typeTheme: DataStoreTypeTheme)

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore("amv_simple_app_note_preferences")

    val layoutManagerFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val type_layout_manager = DataStoreStyleListNotes.valueOf(
                preferences[PreferenceKeys.TYPE_LAYOUT_MANAGER] ?: DataStoreStyleListNotes.GRID.name
            )
            LayoutManagerPreferences(type_layout_manager)
        }

    val languageFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val language_app = DataStoreLanguage.valueOf(
                preferences[PreferenceKeys.TYPE_LANGUAGE_APP] ?: DataStoreLanguage.ENG.name
            )
            LanguageAppPreferences(language_app)
        }

    val formatDataTimeFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val format_data_time = DataStoreFormatDateTime.valueOf(
                preferences[PreferenceKeys.TYPE_FORMAT_DATE_TIME] ?: DataStoreFormatDateTime.PATTERN_1.name
            )
            FormatDateTimePreferences(format_data_time)
        }

    val themeFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val theme_app = DataStoreTypeTheme.valueOf(
                preferences[PreferenceKeys.TYPE_THEME_APP] ?: DataStoreTypeTheme.SYSTEM.name
            )
            ThemeAppPreferences(theme_app)
        }

    suspend fun updateTypeLayoutManager(dataStoreStyleListNotes: DataStoreStyleListNotes) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.TYPE_LAYOUT_MANAGER] = dataStoreStyleListNotes.name
        }
    }

    suspend fun updateTypeLanguageApp(dataStoreLanguage: DataStoreLanguage) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.TYPE_LANGUAGE_APP] = dataStoreLanguage.name
        }
    }

    suspend fun updateTypeFormatDateTime(dataStoreFormatDateTime: DataStoreFormatDateTime) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.TYPE_FORMAT_DATE_TIME] = dataStoreFormatDateTime.name
        }
    }

    suspend fun updateThemeApp(dataStoreTypeTheme: DataStoreTypeTheme) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.TYPE_THEME_APP] = dataStoreTypeTheme.name
        }
    }

    private object PreferenceKeys {
        val TYPE_LAYOUT_MANAGER = preferencesKey<String>("type_layout_manager")
        val TYPE_LANGUAGE_APP = preferencesKey<String>("type_language_app")
        val TYPE_FORMAT_DATE_TIME = preferencesKey<String>("type_format_date_time")
        val TYPE_THEME_APP = preferencesKey<String>("type_theme_app")
    }
}
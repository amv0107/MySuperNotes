package com.amv.simple.app.mysupernotes.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.emptyPreferences
import androidx.datastore.preferences.preferencesKey
import com.amv.simple.app.mysupernotes.domain.util.TypeLayoutManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "PreferencesManager"

data class LayoutManagerPreferences(val layoutManager: TypeLayoutManager)

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore("amv_simple_app_note_preferences")

    val preferencesFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException){
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else{
                throw exception
            }
        }
        .map { preferences ->
            val type_layout_manager = TypeLayoutManager.valueOf(
                preferences[PreferenceKeys.TYPE_LAYOUT_MANAGER] ?: TypeLayoutManager.STAGGERED_GRID_LAYOUT_MANAGER.name
            )
            LayoutManagerPreferences(type_layout_manager)
        }

    suspend fun updateTypeLayoutManager(typeLayoutManager: TypeLayoutManager) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.TYPE_LAYOUT_MANAGER] = typeLayoutManager.name
        }
    }

    private object PreferenceKeys {
        val TYPE_LAYOUT_MANAGER = preferencesKey<String>("type_layout_manager")
    }
}
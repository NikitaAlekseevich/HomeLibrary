package com.example.homelibrary.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private val Context.dataStore by preferencesDataStore("settings")

class ThemeSettings(private val context: Context) {

    private val dataStore = context.dataStore

    companion object {
        private val THEME_KEY = booleanPreferencesKey("theme_key")
    }

    val isDarkTheme: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[THEME_KEY] ?: false
        }

    suspend fun saveThemeSetting(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkMode
        }
    }
}

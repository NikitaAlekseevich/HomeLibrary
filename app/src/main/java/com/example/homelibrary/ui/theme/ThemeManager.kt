package com.example.homelibrary.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

object ThemeManager {
    private const val PREFS_NAME = "theme_prefs"
    private const val THEME_KEY = "theme_key"

    fun applyTheme(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isDarkTheme = prefs.getBoolean(THEME_KEY, false)
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    fun toggleTheme(context: Context) {
        val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isDarkTheme = prefs.getBoolean(THEME_KEY, false)
        prefs.edit().putBoolean(THEME_KEY, !isDarkTheme).apply()
        applyTheme(context)
    }
}

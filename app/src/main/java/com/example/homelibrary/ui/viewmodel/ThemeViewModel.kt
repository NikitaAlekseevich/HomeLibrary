package com.example.homelibrary.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homelibrary.data.ThemeSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ThemeViewModel(application: Application) : AndroidViewModel(application) {
    private val themeSettings = ThemeSettings(application)

    val isDarkTheme: Flow<Boolean> = themeSettings.isDarkTheme

    fun saveThemeSetting(isDarkMode: Boolean) {
        viewModelScope.launch {
            themeSettings.saveThemeSetting(isDarkMode)
        }
    }
}

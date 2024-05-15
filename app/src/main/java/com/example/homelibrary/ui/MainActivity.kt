package com.example.homelibrary.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.example.homelibrary.ui.theme.HomeLibraryTheme
import com.example.homelibrary.ui.theme.ThemeManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeManager.applyTheme(this)
        setContent {
            HomeLibraryTheme {
                Surface {
                    MainScreen()
                }
            }
        }
    }
}
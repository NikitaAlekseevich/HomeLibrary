package com.example.homelibrary.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.homelibrary.ui.screens.HomeScreen
import com.example.homelibrary.ui.screens.AddEditBookScreen
import com.example.homelibrary.ui.theme.HomeLibraryTheme

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("addEditBook") { AddEditBookScreen(navController) }
        // Другие маршруты...
    }
}
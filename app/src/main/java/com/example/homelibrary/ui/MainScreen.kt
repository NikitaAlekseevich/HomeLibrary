package com.example.homelibrary.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.homelibrary.ui.viewmodel.BookViewModel
import com.example.homelibrary.ui.viewmodel.ThemeViewModel


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val bookViewModel: BookViewModel = viewModel()
    val themeViewModel: ThemeViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, bookViewModel, themeViewModel)
        }
        composable("addEditBook/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId") ?: -1
            AddEditBookScreen(navController, bookViewModel, themeViewModel, bookId)
        }
        composable("statistics") {
            StatisticsScreen(navController, bookViewModel, themeViewModel)
        }
        composable("searchBooks") {
            SearchBooksScreen(navController, bookViewModel, themeViewModel)
        }
    }
}





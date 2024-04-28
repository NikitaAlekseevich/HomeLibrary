package com.example.homelibrary.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.homelibrary.ui.viewmodel.BookViewModel
import androidx.compose.runtime.livedata.observeAsState


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val bookViewModel: BookViewModel = viewModel() // Получение ViewModel

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, bookViewModel)
        }
        composable("addEditBook") {
            AddEditBookScreen(navController, bookViewModel, null)
        }
        composable(
            route = "addEditBook/{bookId}",
            arguments = listOf(navArgument("bookId") {
                type = NavType.IntType
                defaultValue = -1 // Установите значение по умолчанию, если используете
            })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId") ?: -1
            // Подписываемся на LiveData и получаем текущее значение
            val book by bookViewModel.getBookById(bookId).observeAsState()
            AddEditBookScreen(navController, bookViewModel, book)
        }
        // Остальные маршруты...
    }
}



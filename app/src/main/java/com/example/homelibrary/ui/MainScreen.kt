package com.example.homelibrary.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.homelibrary.ui.viewmodel.AuthViewModel
import com.example.homelibrary.ui.viewmodel.BookViewModel
import com.example.homelibrary.ui.viewmodel.ThemeViewModel


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val isUserAuthenticated by authViewModel.isUserAuthenticated.collectAsState()

    LaunchedEffect(isUserAuthenticated) {
        if (isUserAuthenticated) {
            navController.navigate("home") {
                popUpTo("signIn") { inclusive = true }
            }
        } else {
            navController.navigate("signIn") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    NavHost(navController = navController, startDestination = "signIn") {
        composable("signIn") {
            SignInScreen(authViewModel, navController)
        }
        composable("signUp") {
            SignUpScreen(authViewModel, navController)
        }
        composable("home") {
            val bookViewModel: BookViewModel = viewModel()
            val themeViewModel: ThemeViewModel = viewModel()
            HomeScreen(navController, bookViewModel)
        }
        composable("addEditBook/{bookId}",
            arguments = listOf(navArgument("bookId") { type = NavType.IntType })
        ) { backStackEntry ->
            val bookId = backStackEntry.arguments?.getInt("bookId") ?: -1
            val bookViewModel: BookViewModel = viewModel()
            val themeViewModel: ThemeViewModel = viewModel()
            AddEditBookScreen(navController, bookViewModel, themeViewModel, bookId)
        }
        composable("statistics") {
            val bookViewModel: BookViewModel = viewModel()
            val themeViewModel: ThemeViewModel = viewModel()
            StatisticsScreen(navController, bookViewModel, themeViewModel)
        }
        composable("searchBooks") {
            val bookViewModel: BookViewModel = viewModel()
            val themeViewModel: ThemeViewModel = viewModel()
            SearchBooksScreen(navController, bookViewModel, themeViewModel)
        }
    }
}



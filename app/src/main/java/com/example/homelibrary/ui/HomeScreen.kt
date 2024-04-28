package com.example.homelibrary.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.homelibrary.ui.viewmodel.BookViewModel

// Экран со списком книг
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, bookViewModel: BookViewModel) {
    Scaffold(topBar = { TopAppBar(title = { Text("Моя Библиотека") }) }, floatingActionButton = {
        FloatingActionButton(onClick = { navController.navigate("addEditBook") }) {
            Icon(Icons.Filled.Add, contentDescription = "Добавить книгу")
        }
    }) { innerPadding ->
        BodyContent(Modifier.padding(innerPadding), navController)
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BodyContent(modifier: Modifier = Modifier, navController: NavController) {
    // Состояние для хранения текста поискового запроса
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Поиск книг...", fontFamily = FontFamily.Monospace) },
            trailingIcon = {
                if (searchText.text.isNotEmpty()) {
                    IconButton(onClick = {
                        searchText = TextFieldValue("")
                        keyboardController?.hide()
                    }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Очистить")
                    }
                }
            },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Здесь будет реализация списка книг
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Список книг пока пуст")
        }
    }
}
package com.example.homelibrary.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.homelibrary.ui.viewmodel.BookViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBooksScreen(navController: NavController, bookViewModel: BookViewModel) {
    var searchText by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Поиск книг") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Введите название книги") },
                    trailingIcon = {
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = { searchText = "" }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Очистить")
                            }
                        }
                    },
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { bookViewModel.searchBooks(searchText) },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Поиск")
                }
                Spacer(modifier = Modifier.height(16.dp))
                SearchResultList(bookViewModel)
            }
        }
    )
}

@Composable
fun SearchResultList(bookViewModel: BookViewModel) {
    val searchResults by bookViewModel.searchResults.observeAsState(initial = emptyList())
    val searchError by bookViewModel.searchError.observeAsState(initial = false)
    val context = LocalContext.current

    if (searchResults.isEmpty() && !searchError) {
        Text("Нет результатов", style = MaterialTheme.typography.bodyMedium)
    } else if (searchError) {
        Column {
            Text(
                "Ошибка при поиске. Попробуйте снова.",
                style = MaterialTheme.typography.bodyMedium
            )
            Button(onClick = { bookViewModel.retrySearch() }) {
                Text("Обновить")
            }
        }
    } else {
        LazyColumn {
            items(searchResults) { book ->
                Text(book.title, style = MaterialTheme.typography.bodyMedium)
                Text("Автор: ${book.author}", style = MaterialTheme.typography.bodyMedium)
                Text("Страниц: ${book.pageCount}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}



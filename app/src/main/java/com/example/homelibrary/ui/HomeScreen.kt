package com.example.homelibrary.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.homelibrary.R
import com.example.homelibrary.model.Book
import com.example.homelibrary.ui.viewmodel.BookViewModel

// Экран со списком книг
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, bookViewModel: BookViewModel) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Моя Библиотека") }) },
        bottomBar = {
            BottomAppBar(
                contentPadding = PaddingValues(horizontal = 16.dp),
                content = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FloatingActionButton(
                            onClick = { navController.navigate("statistics") },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(painter = painterResource(id = R.drawable.ic_chart), contentDescription = "Статистика")
                        }
                        FloatingActionButton(
                            onClick = { navController.navigate("addEditBook/-1") },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = "Добавить книгу")
                        }
                        FloatingActionButton(
                            onClick = { navController.navigate("searchBooks") },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Поиск книг")
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        BodyContent(Modifier.padding(innerPadding), navController, bookViewModel)
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BodyContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    bookViewModel: BookViewModel
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val books = bookViewModel.books.observeAsState(listOf())
    val searchTextState = searchText.text


    val filteredBooks = books.value.filter {
        it.title.contains(searchTextState, ignoreCase = true) ||
                it.author.contains(searchTextState, ignoreCase = true) ||
                it.genre.contains(searchTextState, ignoreCase = true)
    }

    Column(modifier = modifier.padding(16.dp)) {
        OutlinedTextField(
            value = searchText.text,
            onValueChange = { searchText = TextFieldValue(it) },
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

        // Проверяем, есть ли книги для отображения
        if (books.value.isNotEmpty()) {
            LazyColumn {
                items(filteredBooks) { book ->
                    BookItem(book, navController, bookViewModel)
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Список книг пока пуст")
            }
        }
    }
}

@Composable
fun BookItem(book: Book, navController: NavController, bookViewModel: BookViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("addEditBook/${book.id}") }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Название: ${book.title}", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Автор: ${book.author}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Жанр: ${book.genre}", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Количество страниц: ${book.pageCount}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = { bookViewModel.deleteBook(book) }) {
                Icon(Icons.Filled.Delete, contentDescription = "Удалить книгу")
            }
        }
    }
}

package com.example.homelibrary.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.homelibrary.R
import com.example.homelibrary.data.ThemeSettings
import com.example.homelibrary.model.Book
import com.example.homelibrary.ui.viewmodel.BookViewModel
import kotlinx.coroutines.launch

// Экран со списком книг
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    bookViewModel: BookViewModel
) {
    val context = LocalContext.current
    val themeSettings = remember { ThemeSettings(context) }
    val isDarkTheme by themeSettings.isDarkTheme.collectAsState(initial = false)

    val scope = rememberCoroutineScope()

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.my_library)) },
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                themeSettings.saveThemeSetting(!isDarkTheme)
                            }
                        }) {
                            Icon(
                                painter = painterResource(
                                    id = if (isDarkTheme) R.drawable.brightness_4 else R.drawable.brightness_7
                                ),
                                contentDescription = stringResource(id = R.string.theme_switcher)
                            )
                        }
                    }
                )
            },
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
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_chart),
                                    contentDescription = stringResource(id = R.string.statistics)
                                )
                            }
                            FloatingActionButton(
                                onClick = { navController.navigate("addEditBook/-1") },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = stringResource(id = R.string.add_book)
                                )
                            }
                            FloatingActionButton(
                                onClick = { navController.navigate("searchBooks") },
                                modifier = Modifier.size(48.dp)
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = stringResource(id = R.string.search_books)
                                )
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            BodyContent(Modifier.padding(innerPadding), navController, bookViewModel)
        }
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
            placeholder = {
                Text(
                    stringResource(id = R.string.search_books_placeholder),
                    fontFamily = FontFamily.Monospace
                )
            },
            trailingIcon = {
                if (searchText.text.isNotEmpty()) {
                    IconButton(onClick = {
                        searchText = TextFieldValue("")
                        keyboardController?.hide()
                    }) {
                        Icon(
                            Icons.Filled.Clear,
                            contentDescription = stringResource(id = R.string.clear)
                        )
                    }
                }
            },
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (books.value.isNotEmpty()) {
            LazyColumn {
                items(filteredBooks) { book ->
                    BookItem(book, navController, bookViewModel)
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(stringResource(id = R.string.book_list_empty))
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
                Text(
                    text = stringResource(id = R.string.book_title_template, book.title),
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.book_author_template, book.author),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = stringResource(id = R.string.book_genre_template, book.genre),
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(id = R.string.book_page_count_template, book.pageCount),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            IconButton(onClick = { bookViewModel.deleteBook(book) }) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = stringResource(id = R.string.delete_book)
                )
            }
        }
    }
}
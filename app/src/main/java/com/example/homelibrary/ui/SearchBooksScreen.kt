package com.example.homelibrary.ui

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.homelibrary.R
import com.example.homelibrary.ui.viewmodel.BookViewModel
import com.example.homelibrary.ui.viewmodel.ThemeViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBooksScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    themeViewModel: ThemeViewModel
) {
    var searchText by rememberSaveable { mutableStateOf("") }
    val searchHistory by bookViewModel.searchHistory.observeAsState(emptyList())
    val loading by bookViewModel.loading.observeAsState(false)
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState(initial = false)

    LaunchedEffect(searchText) {
        delay(2000)
        if (searchText.isNotEmpty()) {
            bookViewModel.searchBooks(searchText)
        }
    }

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(id = R.string.search_books_title)) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = stringResource(id = R.string.back)
                            )
                        }
                    }
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    TextField(
                        value = searchText,
                        onValueChange = { searchText = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(stringResource(id = R.string.enter_book_title)) },
                        trailingIcon = {
                            if (searchText.isNotEmpty()) {
                                IconButton(onClick = { searchText = "" }) {
                                    Icon(
                                        Icons.Filled.Clear,
                                        contentDescription = stringResource(id = R.string.clear)
                                    )
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
                        Text(stringResource(id = R.string.search))
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    if (loading) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                    }

                    Text(
                        stringResource(id = R.string.search_history),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    LazyColumn {
                        items(searchHistory) { query ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = query,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { searchText = query },
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                IconButton(onClick = { bookViewModel.removeSearchQuery(query) }) {
                                    Icon(
                                        Icons.Default.Clear,
                                        contentDescription = stringResource(id = R.string.clear)
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    SearchResultList(bookViewModel)
                }
            }
        )
    }
}

@Composable
fun SearchResultList(bookViewModel: BookViewModel) {
    val searchResults by bookViewModel.searchResults.observeAsState(initial = emptyList())
    val searchError by bookViewModel.searchError.observeAsState(initial = false)

    if (searchResults.isEmpty() && !searchError) {
        Text(stringResource(id = R.string.no_results), style = MaterialTheme.typography.bodyMedium)
    } else if (searchError) {
        Column {
            Text(
                stringResource(id = R.string.search_error),
                style = MaterialTheme.typography.bodyMedium
            )
            Button(onClick = { bookViewModel.retrySearch() }) {
                Text(stringResource(id = R.string.refresh))
            }
        }
    } else {
        LazyColumn {
            items(searchResults) { book ->
                Text(book.title, style = MaterialTheme.typography.bodyMedium)
                Text(
                    "${stringResource(id = R.string.author)}: ${book.author}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "${stringResource(id = R.string.page_count)}: ${book.pageCount}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
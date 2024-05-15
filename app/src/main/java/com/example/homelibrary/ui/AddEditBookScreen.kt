package com.example.homelibrary.ui

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.homelibrary.R
import com.example.homelibrary.model.Book
import java.text.SimpleDateFormat
import com.example.homelibrary.ui.viewmodel.BookViewModel
import com.example.homelibrary.ui.viewmodel.ThemeViewModel
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditBookScreen(
    navController: NavController,
    bookViewModel: BookViewModel,
    themeViewModel: ThemeViewModel,
    bookId: Int
) {
    val genres = listOf(
        stringResource(R.string.genre_fiction),
        stringResource(R.string.genre_novel),
        stringResource(R.string.genre_poetry),
        stringResource(R.string.genre_history),
        stringResource(R.string.genre_scientific_literature),
        stringResource(R.string.genre_detective),
        stringResource(R.string.genre_other)
    )

    val context = LocalContext.current
    val book by bookViewModel.getBookById(bookId).observeAsState()

    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf(genres.first()) }
    var pageCount by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    var toastMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(book) {
        book?.let {
            title = it.title
            author = it.author
            selectedGenre = it.genre
            pageCount = it.pageCount.toString()
            startDate = it.startDate?.let { date ->
                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date)
            } ?: ""
            endDate = it.endDate?.let { date ->
                SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date)
            } ?: ""
            note = it.note ?: ""
        }
    }

    LaunchedEffect(toastMessage) {
        toastMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            toastMessage = null
        }
    }

    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState(initial = false)

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
    ) {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text(stringResource(R.string.book_title)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text(stringResource(R.string.author)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                GenreSelector(genres, selectedGenre) { selectedGenre = it }
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = pageCount,
                    onValueChange = { pageCount = it },
                    label = { Text(stringResource(R.string.page_count)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text(stringResource(R.string.start_date)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    label = { Text(stringResource(R.string.end_date)) },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text(stringResource(R.string.note)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 5
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(
                        onClick = {
                            if (title.isNotEmpty() && author.isNotEmpty() && pageCount.isNotEmpty()) {
                                try {
                                    val pageCountInt = pageCount.toInt()
                                    val startDateResult = parseDate(startDate)
                                    val endDateResult = parseDate(endDate)

                                    if (startDateResult.isSuccess && endDateResult.isSuccess) {
                                        // Если у книги уже есть ID, обновляем её, иначе добавляем новую
                                        val newOrUpdatedBook = if (bookId != -1) {
                                            book?.copy(
                                                id = bookId,
                                                title = title,
                                                author = author,
                                                genre = selectedGenre,
                                                pageCount = pageCountInt,
                                                startDate = startDateResult.getOrNull(),
                                                endDate = endDateResult.getOrNull(),
                                                note = note
                                            )
                                        } else {
                                            Book(
                                                id = 0, // ID будет сгенерирован базой данных
                                                title = title,
                                                author = author,
                                                genre = selectedGenre,
                                                pageCount = pageCountInt,
                                                startDate = startDateResult.getOrNull(),
                                                endDate = endDateResult.getOrNull(),
                                                note = note
                                            )
                                        }

                                        if (newOrUpdatedBook != null) {
                                            bookViewModel.addOrUpdateBook(newOrUpdatedBook)
                                        }
                                        navController.navigateUp()
                                    } else {
                                        toastMessage = context.getString(R.string.toast_date_format)
                                    }
                                } catch (e: NumberFormatException) {
                                    toastMessage = context.getString(R.string.toast_page_count)
                                }
                            } else {
                                toastMessage = context.getString(R.string.toast_fill_fields)
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.save))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            navController.navigateUp()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenreSelector(genres: List<String>, selectedGenre: String, onGenreSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(selectedGenre) }

    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = selectedText,
            onValueChange = { },
            readOnly = true, // Поле только для чтения
            label = { Text(stringResource(R.string.genre)) },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                        contentDescription = if (expanded) "Скрыть жанры" else "Показать жанры"
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            genres.forEach { genre ->
                DropdownMenuItem(text = { Text(text = genre) },
                    onClick = {
                        selectedText = genre
                        onGenreSelected(genre)
                        expanded = false
                    })
            }
        }
    }
}

fun parseDate(dateStr: String): Result<Date> {
    return try {
        Result.success(SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(dateStr)!!)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
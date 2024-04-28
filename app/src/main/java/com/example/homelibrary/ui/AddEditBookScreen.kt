package com.example.homelibrary.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.homelibrary.model.Book
import java.text.SimpleDateFormat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homelibrary.ui.viewmodel.BookViewModel
import java.util.*

val genres = listOf("Фантастика", "Роман", "Поэзия", "История", "Научная литература", "Детектив")

@Composable
fun AddEditBookScreen(
    navController: NavController,
    viewModel: BookViewModel,
    bookId: Int
) {
    val book by viewModel.getBookById(bookId).observeAsState()

    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var selectedGenre by remember { mutableStateOf(genres.first()) }
    var pageCount by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Название книги") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = author,
            onValueChange = { author = it },
            label = { Text("Автор") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        GenreSelector(genres, selectedGenre) { selectedGenre = it }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = pageCount,
            onValueChange = { pageCount = it },
            label = { Text("Количество страниц") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = startDate,
            onValueChange = { startDate = it },
            label = { Text("Дата начала чтения") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = endDate,
            onValueChange = { endDate = it },
            label = { Text("Дата окончания чтения") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Заметка") },
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
                            val startDateParsed = parseDate(startDate)
                            val endDateParsed = parseDate(endDate)

                            if (bookId != -1) {
                                val updatedBook = book?.copy(
                                    id = bookId,
                                    title = title,
                                    author = author,
                                    genre = selectedGenre,
                                    pageCount = pageCountInt,
                                    startDate = startDateParsed,
                                    endDate = endDateParsed,
                                    note = note
                                )
                                if (updatedBook != null) {
                                    viewModel.updateBook(updatedBook)
                                }
                            } else { // Если ID нет, значит это новая книга, и мы её добавляем
                                val newBook = Book(
                                    title = title,
                                    author = author,
                                    genre = selectedGenre,
                                    pageCount = pageCountInt,
                                    startDate = startDateParsed,
                                    endDate = endDateParsed,
                                    note = note
                                )
                                viewModel.addBook(newBook)
                            }
                            navController.navigateUp()
                        } catch (e: NumberFormatException) {
                            // Обработка ошибки ввода числа страниц
                        }
                    } else {
                        // Отображение ошибки, если обязательные поля пустые
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Сохранить")
            }


            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    navController.navigateUp()
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Отмена")
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
            label = { Text("Выберите жанр") },
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


fun parseDate(dateStr: String): Date? {
    return try {
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).parse(dateStr)
    } catch (e: Exception) {
        null
    }
}

//@Composable
//fun AddEditBookScreen(navController: NavController) {
//    Text(text = "Добавить/Редактировать книгу")
//}

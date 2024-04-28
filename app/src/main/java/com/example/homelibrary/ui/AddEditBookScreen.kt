package com.example.homelibrary.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.homelibrary.model.Book
import java.text.SimpleDateFormat
import kotlin.random.Random
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.homelibrary.ui.viewmodel.BookViewModel
import java.util.*

val genres = listOf("Фантастика", "Роман", "Поэзия", "История", "Научная литература", "Детектив")

@Composable
fun AddEditBookScreen(
    navController: NavController,
    viewModel: BookViewModel = viewModel(),
    book: Book? = null // добавлен параметр с дефолтным значением null
) {
    var title by remember { mutableStateOf(book?.title ?: "") }
    var author by remember { mutableStateOf(book?.author ?: "") }
    var selectedGenre by remember { mutableStateOf(book?.genre ?: genres.first()) }
    var pageCount by remember { mutableStateOf(book?.pageCount?.toString() ?: "") }
    var startDate by remember {
        mutableStateOf(book?.startDate?.let {
            SimpleDateFormat(
                "dd.MM.yyyy",
                Locale.getDefault()
            ).format(it)
        } ?: "")
    }
    var endDate by remember {
        mutableStateOf(book?.endDate?.let {
            SimpleDateFormat(
                "dd.MM.yyyy",
                Locale.getDefault()
            ).format(it)
        } ?: "")
    }
    var note by remember { mutableStateOf(book?.note ?: "") }

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
                            navController.navigateUp() // Возвращаемся назад после добавления
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
                    // Просто возвращаемся назад без сохранения изменений
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

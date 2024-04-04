package com.example.homelibrary.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.homelibrary.model.Book
import java.text.SimpleDateFormat
import java.util.*

val genres = listOf("Фантастика", "Роман", "Поэзия", "История", "Научная литература", "Детектив")

@Composable
fun AddEditBookScreen(navController: NavController, book: Book? = null) {
    var title by remember { mutableStateOf(book?.title ?: "") }
    var author by remember { mutableStateOf(book?.author ?: "") }
    var selectedGenre by remember { mutableStateOf(book?.genre ?: genres.first()) }
    var pageCount by remember { mutableStateOf(book?.pageCount?.toString() ?: "") }
    var startDate by remember { mutableStateOf(book?.startDate?.let { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(it) } ?: "") }
    var endDate by remember { mutableStateOf(book?.endDate?.let { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(it) } ?: "") }
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

        GenreDropdown(genres, selectedGenre) { selectedGenre = it }
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
            // textStyle здесь использовать некорректно для изменения стиля вводимого текста.
            // Если нужно изменить стиль подписи (label), используйте параметр label.
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(
                onClick = {
                    // Обработка сохранения книги
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Сохранить")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    // Обработка отмены изменений
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
fun GenreDropdown(genres: List<String>, selectedGenre: String, onGenreSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            readOnly = true,
            value = selectedGenre,
            onValueChange = { },
            label = { Text("Жанр") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            genres.forEach { genre ->
                DropdownMenuItem(
                    text = { Text(genre) },
                    onClick = {
                        onGenreSelected(genre)
                        expanded = false
                    }
                )
            }
        }
    }
}


//@Composable
//fun AddEditBookScreen(navController: NavController) {
//    Text(text = "Добавить/Редактировать книгу")
//}

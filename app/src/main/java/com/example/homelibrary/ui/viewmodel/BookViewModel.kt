package com.example.homelibrary.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.homelibrary.data.local.AppDatabase
import com.example.homelibrary.model.Book
import kotlinx.coroutines.launch



class BookViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "library.db"
    ).build()

    private val bookDao = db.bookDao()

    val books: LiveData<List<Book>> = bookDao.getAllBooks()

    fun addBook(book: Book) {
        viewModelScope.launch {
            bookDao.insertBook(book)
        }
    }

    fun getBookById(bookId: Int): LiveData<Book> {
        return bookDao.getBookById(bookId)
    }
}

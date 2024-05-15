package com.example.homelibrary.data.repository

import androidx.lifecycle.LiveData
import com.example.homelibrary.data.local.BookDao
import com.example.homelibrary.model.Book
import kotlinx.coroutines.flow.Flow
import java.util.Date

class BookRepository(private val bookDao: BookDao) {

    val allBooks: Flow<List<Book>> = bookDao.getAllBooks()

    suspend fun insert(book: Book) {
        bookDao.insertBook(book)
    }

    suspend fun update(book: Book) {
        bookDao.updateBook(book)
    }

    suspend fun delete(book: Book) {
        bookDao.deleteBook(book)
    }

    fun getBookById(bookId: Int): LiveData<Book?> {
        return bookDao.getBookById(bookId)
    }

}

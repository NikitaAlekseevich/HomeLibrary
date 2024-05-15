package com.example.homelibrary.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.homelibrary.model.Book
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAllBooks(): Flow<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Query("SELECT * FROM books WHERE id = :bookId")
    fun getBookById(bookId: Int): LiveData<Book?>

    @Query("SELECT * FROM books WHERE startDate BETWEEN :start AND :end")
    fun getBooksBetweenDates(start: Date, end: Date): Flow<List<Book>>
}

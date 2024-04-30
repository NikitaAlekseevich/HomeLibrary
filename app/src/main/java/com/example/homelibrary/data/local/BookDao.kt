package com.example.homelibrary.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.homelibrary.model.Book
import java.util.Date

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getAllBooks(): LiveData<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: Book)

    @Delete
    suspend fun deleteBook(book: Book)

    @Update
    suspend fun updateBook(book: Book)

    @Query("SELECT * FROM books WHERE id = :bookId LIMIT 1")
    fun getBookById(bookId: Int): LiveData<Book?>

    @Query("SELECT COUNT(*) FROM books WHERE endDate BETWEEN :start AND :end")
    fun getBooksReadBetweenDates(start: Date, end: Date): LiveData<Int>

    @Query("SELECT COUNT(*) FROM books")
    fun getTotalBooksRead(): LiveData<Int>

}

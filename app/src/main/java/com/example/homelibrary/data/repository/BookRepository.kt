package com.example.homelibrary.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.homelibrary.data.local.BookDao
import com.example.homelibrary.data.remote.OpenLibraryApi
import com.example.homelibrary.model.ApiBook
import com.example.homelibrary.model.Book
import com.example.homelibrary.model.BookSearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class BookRepository(private val bookDao: BookDao, private val openLibraryApi: OpenLibraryApi) {
    val searchResults = MutableLiveData<List<ApiBook>>()

    fun getAllBooks(): LiveData<List<Book>> = bookDao.getAllBooks()

    // Запрос к API для поиска книг
    fun searchBooks(query: String) {
        openLibraryApi.searchBooks(query).enqueue(object : Callback<BookSearchResponse> {
            override fun onResponse(
                call: Call<BookSearchResponse>,
                response: Response<BookSearchResponse>
            ) {
                if (response.isSuccessful) {
                    val apiBooks = response.body()?.docs?.map { doc ->
                        ApiBook(
                            title = doc.title,
                            author = doc.author?.joinToString(", ") ?: "Unknown",
                            pageCount = doc.pageCount ?: 0
                        )
                    } ?: emptyList()
                    searchResults.postValue(apiBooks)
                } else {
                    searchResults.postValue(emptyList())
                }
            }

            override fun onFailure(call: Call<BookSearchResponse>, t: Throwable) {
                searchResults.postValue(emptyList())
            }
        })
    }
}

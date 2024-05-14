package com.example.homelibrary.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.homelibrary.data.SearchHistoryManager
import com.example.homelibrary.data.local.AppDatabase
import com.example.homelibrary.data.remote.OpenLibraryApi
import com.example.homelibrary.model.ApiBook
import com.example.homelibrary.model.Book
import com.example.homelibrary.model.BookSearchResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class BookViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(application, AppDatabase::class.java, "library.db").build()
    private val bookDao = db.bookDao()
    val books: LiveData<List<Book>> = bookDao.getAllBooks()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://openlibrary.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(OpenLibraryApi::class.java)
    private val _searchResults = MutableLiveData<List<ApiBook>>()
    val searchResults: LiveData<List<ApiBook>> = _searchResults

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    private var lastSearchQuery: String? = null
    private val _searchError = MutableLiveData<Boolean>(false)
    val searchError: LiveData<Boolean> = _searchError

    private val searchHistoryManager = SearchHistoryManager(application)
    private val _searchHistory = MutableLiveData<List<String>>(searchHistoryManager.getSearchHistory().toList())
    val searchHistory: LiveData<List<String>> = _searchHistory

    fun searchBooks(query: String) {
        lastSearchQuery = query
        _searchError.value = false
        _loading.value = true
        searchHistoryManager.saveQuery(query)
        _searchHistory.value = searchHistoryManager.getSearchHistory().toList()
        api.searchBooks(query).enqueue(object : Callback<BookSearchResponse> {
            override fun onResponse(
                call: Call<BookSearchResponse>,
                response: Response<BookSearchResponse>
            ) {
                _loading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _searchResults.value = response.body()?.docs?.map {
                        ApiBook(
                            title = it.title,
                            author = it.author?.joinToString(", ") ?: "Unknown",
                            pageCount = it.pageCount ?: 0
                        )
                    } ?: listOf()
                } else {
                    _searchError.value = true
                }
            }

            override fun onFailure(call: Call<BookSearchResponse>, t: Throwable) {
                _loading.value = false
                _searchError.value = true
            }
        })
    }

    fun retrySearch() {
        lastSearchQuery?.let { searchBooks(it) }
    }

    fun removeSearchQuery(query: String) {
        searchHistoryManager.removeQuery(query)
        _searchHistory.value = searchHistoryManager.getSearchHistory().toList()
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch { bookDao.deleteBook(book) }
    }

    fun addOrUpdateBook(book: Book) {
        viewModelScope.launch {
            if (book.id == 0) bookDao.insertBook(book)
            else bookDao.updateBook(book)
        }
    }

    fun getBookById(bookId: Int): LiveData<Book?> {
        return if (bookId == -1) MutableLiveData(null)
        else bookDao.getBookById(bookId)
    }
}
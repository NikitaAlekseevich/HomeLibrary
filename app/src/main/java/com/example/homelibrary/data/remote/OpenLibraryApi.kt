package com.example.homelibrary.data.remote

import com.example.homelibrary.model.BookSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenLibraryApi {
    @GET("search.json")
    fun searchBooks(@Query("q") query: String): Call<BookSearchResponse>
}


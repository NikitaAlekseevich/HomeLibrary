package com.example.homelibrary.model

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class ApiBook(
    val title: String,
    val author: String,
    val pageCount: Int
)


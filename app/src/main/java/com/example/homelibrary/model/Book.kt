package com.example.homelibrary.model

import java.util.*

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val genre: String,
    val pageCount: Int,
    val startDate: Date?,
    val endDate: Date?,
    val note: String?
)

package com.example.homelibrary.model

import com.google.gson.annotations.SerializedName

data class BookSearchResponse(
    val numFound: Int,
    val start: Int,
    val docs: List<BookDocument>
)

data class BookDocument(
    val title: String,
    @SerializedName("author_name") val author: List<String>?,
    @SerializedName("number_of_pages_median") val pageCount: Int?
)






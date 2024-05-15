package com.example.homelibrary.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "books")
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val author: String,
    val genre: String,
    val pageCount: Int,
    val startDate: Date?,
    val endDate: Date?,
    val note: String
)

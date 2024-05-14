package com.example.homelibrary.data

import android.content.Context
import android.content.SharedPreferences

class SearchHistoryManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("search_history", Context.MODE_PRIVATE)

    fun saveQuery(query: String) {
        val queries = getSearchHistory().toMutableSet()
        queries.add(query)
        sharedPreferences.edit().putStringSet("queries", queries).apply()
    }

    fun getSearchHistory(): Set<String> {
        return sharedPreferences.getStringSet("queries", emptySet()) ?: emptySet()
    }

    fun removeQuery(query: String) {
        val queries = getSearchHistory().toMutableSet()
        queries.remove(query)
        sharedPreferences.edit().putStringSet("queries", queries).apply()
    }
}

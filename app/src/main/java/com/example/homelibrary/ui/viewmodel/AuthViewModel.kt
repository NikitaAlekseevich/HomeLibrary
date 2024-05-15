package com.example.homelibrary.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homelibrary.data.local.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.homelibrary.data.local.UserDao
import com.example.homelibrary.data.local.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val authDao = db.authDao()

    private val _isUserAuthenticated = MutableStateFlow(false)
    val isUserAuthenticated: StateFlow<Boolean> get() = _isUserAuthenticated

    init {
        checkUserAuthentication()
    }

    private fun checkUserAuthentication() {
        viewModelScope.launch {
            _isUserAuthenticated.value = authDao.isUserAuthenticated()
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val user = authDao.getUserByEmailAndPassword(email, password)
            if (user != null) {
                _isUserAuthenticated.value = true
            } else {
                // login failure
            }
        }
    }

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            val user = User(email = email, password = password)
            authDao.insertUser(user)
            _isUserAuthenticated.value = true
        }
    }
}
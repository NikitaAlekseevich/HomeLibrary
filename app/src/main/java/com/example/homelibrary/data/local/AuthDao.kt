package com.example.homelibrary.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
@Dao
interface AuthDao {

    @Query("SELECT * FROM user WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, password: String): User?

    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT EXISTS(SELECT 1 FROM user LIMIT 1)")
    suspend fun isUserAuthenticated(): Boolean
}

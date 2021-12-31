package com.example.FLO.Data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.FLO.Data.User

@Dao
interface UserDao {

    @Insert
    fun insert(user : User)

    @Query("SELECT * FROM UserTable")
    fun getUsers() : List<User>

    @Query("SELECT * FROM UserTable WHERE email == :email AND pw == :pw")
    fun getUser(email :String, pw  : String) : User?

}
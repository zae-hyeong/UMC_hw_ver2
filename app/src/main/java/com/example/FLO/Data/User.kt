package com.example.FLO.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class User(
    var email : String,
    var pw : String
){
    @PrimaryKey(autoGenerate = true) var id : Int = 0
}

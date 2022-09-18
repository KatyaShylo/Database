package com.example.mydatabase.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Employee(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val lastName: String,
    val firstName: String,
    val position: String
)

package com.example.mydatabase.room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EmployeeDao {

    @Query("SELECT * FROM employee")
    fun getAll(): List<Employee>

    @Query("SELECT * FROM employee")
    fun observeStaff() : LiveData<List<Employee>>

    @Insert
    fun insertAll(vararg staff: Employee)

    @Delete
    fun delete(employee: Employee)

    @Update
    fun update(employee: Employee)
}
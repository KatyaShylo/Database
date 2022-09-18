package com.example.mydatabase.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mydatabase.room.Employee
import com.example.mydatabase.room.EmployeeDao

class EmployeeViewModel(
    private val employeeDao: EmployeeDao
) : ViewModel() {

    val liveData: LiveData<List<Employee>> = employeeDao.observeStaff()

    fun onButtonAddClicked(newEmployee: Employee) {
        employeeDao.insertAll(newEmployee)
    }

    fun onSwipeEmployee(position: Int){
        val employeeForDelete = employeeDao.getAll()[position]
        employeeDao.delete(employeeForDelete)
    }

    fun onChangeData(employee: Employee){
        employeeDao.update(employee)
    }
}
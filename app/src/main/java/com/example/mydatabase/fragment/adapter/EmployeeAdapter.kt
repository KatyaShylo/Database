package com.example.mydatabase.fragment.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.mydatabase.databinding.ItemEmployeeBinding
import com.example.mydatabase.fragment.holder.EmployeeViewHolder
import com.example.mydatabase.room.Employee

class EmployeeAdapter(
    context: Context,
    private val onLongItemClicked : (Employee)-> Unit
) : ListAdapter<Employee, EmployeeViewHolder>(DIFF_UTIL) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
        return EmployeeViewHolder(
            binding = ItemEmployeeBinding.inflate(layoutInflater,parent, false),
            onLongItemClicked = onLongItemClicked
        )
    }

    override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    companion object {
        private val DIFF_UTIL = object : DiffUtil.ItemCallback<Employee>() {
            override fun areItemsTheSame(oldItem: Employee, newItem: Employee): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Employee, newItem: Employee): Boolean {
                return (oldItem.firstName == newItem.firstName &&
                        oldItem.lastName == newItem.lastName &&
                        oldItem.position == newItem.position)
            }
        }
    }
}
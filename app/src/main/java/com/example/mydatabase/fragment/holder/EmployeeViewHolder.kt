package com.example.mydatabase.fragment.holder

import androidx.recyclerview.widget.RecyclerView
import com.example.mydatabase.databinding.ItemEmployeeBinding
import com.example.mydatabase.room.Employee

class EmployeeViewHolder(
    private val binding: ItemEmployeeBinding,
    private val onLongItemClicked : (Employee)-> Unit
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(item: Employee) {
        with(binding) {
            textId.text = "Id: ${item.id}"
            textLastName.text = "Last name: ${item.lastName}"
            textFirstName.text = "First name: ${item.firstName}"
            textPosition.text = "Position: ${item.position}"

            root.setOnLongClickListener{
                onLongItemClicked(item)
                true
            }
        }
    }
}
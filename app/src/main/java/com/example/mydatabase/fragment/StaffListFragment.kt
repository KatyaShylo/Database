package com.example.mydatabase.fragment

import android.app.AlertDialog
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mydatabase.R
import com.example.mydatabase.appDatabase
import com.example.mydatabase.databinding.FragmentStaffListBinding
import com.example.mydatabase.fragment.adapter.EmployeeAdapter
import com.example.mydatabase.viewmodel.EmployeeViewModel

class StaffListFragment : Fragment() {

    private var _binding: FragmentStaffListBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val employeeDao by lazy {
        requireContext().appDatabase.employeeDao()
    }

    private val viewModel: EmployeeViewModel by viewModels(
        factoryProducer = {
            viewModelFactory {
                initializer {
                    EmployeeViewModel(employeeDao)
                }
            }
        }
    )

    private val adapter by lazy {
        EmployeeAdapter(requireContext()){ employee->
            findNavController().navigate(StaffListFragmentDirections.toEditDialog(
                employee.id,
                employee.lastName,
                employee.lastName,
                employee.position
            ))

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentStaffListBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            recyclerView.adapter = adapter

            with(toolbarContent) {
                menu
                    .findItem(R.id.search)
                    .actionView
                    .let { it as SearchView }
                    .setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                        override fun onQueryTextSubmit(query: String): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(query: String): Boolean {
                            adapter.submitList(employeeDao.getAll()
                                .filter {
                                    it.firstName.contains(query) || it.lastName.contains(query)
                                            || it.position.contains(query)
                                })
                            return true
                        }
                    })
            }

            recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount) {
                        outRect.top = resources.getDimension(R.dimen.value_for_decorator).toInt()
                    }
                }
            })

            viewModel.liveData.observe(viewLifecycleOwner) {
                updateData()
            }

            val deleteSwipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition

                    AlertDialog.Builder(requireContext())
                        .setTitle(TITLE_DIALOG_DELETE_EMPLOYEE)
                        .setMessage(MESSAGE_DIALOG_DELETE)
                        .setPositiveButton(android.R.string.ok) { _, _ ->
                            viewModel.onSwipeEmployee(position)
                            Toast.makeText(requireContext(), TOAST_TEXT_DELETE, Toast.LENGTH_SHORT)
                                .show()
                        }
                        .setNegativeButton(android.R.string.cancel) { dialog, _ ->
                            adapter.notifyItemInserted(position)
                            adapter.notifyDataSetChanged()
                            Toast.makeText(
                                requireContext(),
                                TOAST_TEXT_NOT_DELETE,
                                Toast.LENGTH_SHORT
                            ).show()
                            dialog.cancel()
                        }
                        .show()
                }
            }
            val itemTouchHelper = ItemTouchHelper(deleteSwipe)
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateData() {
        adapter.submitList(employeeDao.getAll())
    }

    companion object{
        private const val TITLE_DIALOG_DELETE_EMPLOYEE = "Delete employee"
        private const val MESSAGE_DIALOG_DELETE = "Are you sure you want to delete?"
        private const val TOAST_TEXT_DELETE = "Employee has been deleted"
        private const val TOAST_TEXT_NOT_DELETE = "Employee nas not been deleted"
    }
}
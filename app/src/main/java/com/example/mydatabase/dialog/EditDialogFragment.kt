package com.example.mydatabase.dialog

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mydatabase.R
import com.example.mydatabase.appDatabase
import com.example.mydatabase.databinding.FragmentEditDialogBinding
import com.example.mydatabase.room.Employee
import com.example.mydatabase.viewmodel.EmployeeViewModel

class EditDialogFragment : DialogFragment() {

    private var _binging: FragmentEditDialogBinding? = null
    private val binding get() = requireNotNull(_binging)

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

    private val args by navArgs<EditDialogFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEditDialogBinding.inflate(inflater, container, false)
            .also { _binging = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable = false

        with(binding) {

            editTextLastName.setText(args.lastName)
            editTextFirstName.setText(args.firstName)
            editTextPosition.setText(args.position)

            getErrorLastName()
            getErrorFirstName()
            getErrorPosition()

            buttonChange.setOnClickListener {

                val lastName = editTextLastName.text.toString()
                val firstName = editTextFirstName.text.toString()
                val position = editTextPosition.text.toString()

                if (containerLastName.error != null || containerFirstName.error != null
                    || containerPosition.error != null
                ) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Incorrect data entry")
                        .setMessage(
                            "Please, check the data entry!"
                        )
                        .setPositiveButton(android.R.string.ok) { _, _ -> }
                        .show()

                }

                if (containerLastName.error == null && containerFirstName.error == null
                    && containerPosition.error == null
                    && lastName.isNotEmpty() && firstName.isNotEmpty() && position.isNotEmpty()
                ) {
                    viewModel.onChangeData(
                        Employee(id = args.id,
                            lastName = lastName, firstName = firstName, position = position
                        )
                    )
                    Toast.makeText( requireContext(), TOAST_SUCCESSFUL_EDITING, Toast.LENGTH_SHORT)
                        .show()
                    findNavController().navigate(R.id.fragment_staff_list)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binging = null
    }

    private fun getErrorLastName() {
        binding.editTextLastName.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (!text.matches("[a-zA-Z]+".toRegex())) {
                    binding.containerLastName.error = "Use only latin letters"
                } else {
                    binding.containerLastName.error = null
                }
            } else {
                binding.containerLastName.error = "Field is empty"
            }
        }
    }

    private fun getErrorFirstName() {
        binding.editTextFirstName.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (!text.matches("[a-zA-Z]+".toRegex())) {
                    binding.containerFirstName.error = "Use only latin letters"
                } else {
                    binding.containerFirstName.error = null
                }
            } else {
                binding.containerFirstName.error = "Field is empty"
            }
        }
    }

    private fun getErrorPosition() {
        binding.editTextPosition.doOnTextChanged { text, _, _, _ ->
            if (text != null) {
                if (!text.matches("[a-zA-Z\\s]+".toRegex())) {
                    binding.containerPosition.error = "Use only latin letters"
                } else {
                    binding.containerPosition.error = null
                }
            } else {
                binding.containerPosition.error = "Field is empty"
            }
        }
    }

     companion object{
         private const val TOAST_SUCCESSFUL_EDITING = "Employee data has been successfully changed"
     }
}
package com.example.mydatabase.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mydatabase.appDatabase
import com.example.mydatabase.databinding.FragmentAddEmployeeBinding
import com.example.mydatabase.room.Employee
import com.example.mydatabase.viewmodel.EmployeeViewModel

class AddEmployeeFragment : Fragment() {

    private var _binding: FragmentAddEmployeeBinding? = null
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAddEmployeeBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            setNullError()

            getErrorLastName()
            getErrorFirstName()
            getErrorPosition()

            buttonAdd.setOnClickListener {

                val lastName = editTextLastName.text.toString()
                val firstName = editTextFirstName.text.toString()
                val position = editTextPosition.text.toString()

                if (containerLastName.error != null || containerFirstName.error != null
                    || containerPosition.error != null
                ) {
                    AlertDialog.Builder(requireContext())
                        .setTitle(TITLE_DIALOG_INCORRECT_ENTRY)
                        .setMessage(MESSAGE_DIALOG_INCORRECT_ENTRY)
                        .setPositiveButton(android.R.string.ok) { _, _ -> }
                        .show()
                }

                if (containerLastName.error == null && containerFirstName.error == null
                    && containerPosition.error == null
                    && lastName.isNotEmpty() && firstName.isNotEmpty() && position.isNotEmpty()
                ) {
                    viewModel.onButtonAddClicked(
                        Employee(
                            lastName = lastName, firstName = firstName, position = position
                        )
                    )
                    AlertDialog.Builder(requireContext())
                        .setTitle(TITLE_DIALOG_SUCCESSFUL)
                        .setMessage(
                            """
                            Last name: ${editTextLastName.text.toString()}
                            First name: ${editTextFirstName.text.toString()}
                            Position: ${editTextPosition.text.toString()}
                        """.trimIndent()
                        )
                        .setPositiveButton(android.R.string.ok) { _, _ -> }
                        .show()

                    setNullText()

                    setNullError()
                }
            }
            setNullError()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

    private fun setNullText() {
        binding.editTextLastName.setText("")
        binding.editTextFirstName.setText("")
        binding.editTextPosition.setText("")
    }

    private fun setNullError() {
        binding.containerLastName.error = null
        binding.containerFirstName.error = null
        binding.containerPosition.error = null
    }

    companion object {
        private const val TITLE_DIALOG_INCORRECT_ENTRY = "Incorrect data entry"
        private const val MESSAGE_DIALOG_INCORRECT_ENTRY = "Please, check the data entry!"
        private const val TITLE_DIALOG_SUCCESSFUL = "Successful addition"
    }
}
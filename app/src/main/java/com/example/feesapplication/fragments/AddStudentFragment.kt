package com.example.feesapplication.fragments

import android.os.Bundle
import android.view.*
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.feesapplication.R
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.data.viewmodel.SharedViewModel
import com.example.feesapplication.data.viewmodel.StudentViewModel
import com.example.feesapplication.databinding.AddStudentFragmentBinding
import com.google.android.material.chip.Chip


class AddStudentFragment : Fragment() {

    private val args by navArgs<AddStudentFragmentArgs>()

    private val studentViewModel: StudentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private var feeStatusSaved = "None"
    private lateinit var batchNameSaved: String
    private lateinit var currentBatch: Batch

    private var _binding: AddStudentFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onResume() {
        super.onResume()

        // Fees status field adapter
        val feesStatuses = resources.getStringArray(R.array.feesStatuses)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, feesStatuses)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = AddStudentFragmentBinding.inflate(layoutInflater, container, false)


        //Months according to Chips checked
        binding.chipGroup.children.forEach {
            (it as Chip).setOnCheckedChangeListener { buttonView, isChecked ->

            }
        }


        // Set Menu
        setHasOptionsMenu(true)









        binding.autoCompleteTextView.onItemClickListener =
            OnItemClickListener { parent, _, position, _ ->
                val selection = parent.getItemAtPosition(position) as String
                feeStatusSaved = selection
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        batchNameSaved = args.currentBatch.batchName
        currentBatch = args.currentBatch
        binding.batchNameStudent.text = "Batch : $batchNameSaved"

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_student_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save_student) {

            insertStudentToDb()

        }
        return super.onOptionsItemSelected(item)
    }


    private fun insertStudentToDb() {


        val stringMonths = StringBuilder()

        binding.chipGroup.checkedChipIds.forEach {
            when {
                it.equals(binding.janChip.id) -> {
                    stringMonths.append("January, ")
                }
                it.equals(binding.febChip.id) -> {
                    stringMonths.append("February, ")
                }
                it.equals(binding.marChip.id) -> {
                    stringMonths.append("March, ")
                }
                it.equals(binding.aprChip.id) -> {
                    stringMonths.append("April, ")
                }
                it.equals(binding.mayChip.id) -> {
                    stringMonths.append("May, ")
                }
                it.equals(binding.juneChip.id) -> {
                    stringMonths.append("June, ")
                }
                it.equals(binding.julChip.id) -> {
                    stringMonths.append("July, ")
                }
                it.equals(binding.augChip.id) -> {
                    stringMonths.append("August, ")
                }
                it.equals(binding.sepChip.id) -> {
                    stringMonths.append("September, ")
                }
                it.equals(binding.octChip.id) -> {
                    stringMonths.append("October, ")
                }
                it.equals(binding.novChip.id) -> {
                    stringMonths.append("November, ")
                }
                it.equals(binding.decChip.id) -> {
                    stringMonths.append("December")
                }

                else -> {
                    stringMonths.append("Empty")
                }

            }
        }

        val studentNameField = binding.studentNameField.editText?.text.toString()
        val contactNumber = binding.contactNumberField.editText?.text.toString()
        val feesField = binding.feesField.editText?.text.toString()
        val emailField = binding.emailField.editText?.text.toString()


        val validation = sharedViewModel.verifyStudentInputData(
            studentNameField,
            contactNumber,
            feesField,
            feeStatusSaved,
            emailField
        )
        if (validation) {
            // Green signal to save data into database
            val newStudentData = Student(
                studentName = studentNameField,
                studentNumber = contactNumber.toLong(),
                feesAmount = feesField.toLong(),
                feesStatus = sharedViewModel.parseFeesStatus(feeStatusSaved),
                studentEmail = emailField,
                batchName = batchNameSaved,
                monthsPaid = stringMonths.toString()
            )

            studentViewModel.insertStudent(newStudentData)
            Toast.makeText(requireContext(), "Successfully added student!", Toast.LENGTH_SHORT)
                .show()
            val action =
                AddStudentFragmentDirections.actionAddStudentFragment2ToStudentListFragment(
                    currentBatch
                )
            findNavController().navigate(action)
        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT)
                .show()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
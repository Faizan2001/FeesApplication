package com.example.feesapplication.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.feesapplication.R
import com.example.feesapplication.data.database.FeeStatus
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.data.viewmodel.SharedViewModel
import com.example.feesapplication.data.viewmodel.StudentViewModel
import com.example.feesapplication.databinding.FragmentUpdateBinding
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialStyledDatePickerDialog
import java.lang.StringBuilder
import java.util.*


class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    private val studentViewModel: StudentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private var feeStatusSaved = "None"
    private lateinit var currentStudent: Student
    private lateinit var monthsSaved: String
    private lateinit var currentBatch: Batch

    private val MONTHS = listOf<String>("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")


    private var _binding: FragmentUpdateBinding? = null
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
        _binding = FragmentUpdateBinding.inflate(layoutInflater, container, false)
        currentStudent = args.currentStudent
        studentViewModel.getBatchOfStudent(args.currentStudent.batchName)
            .observe(viewLifecycleOwner, {
                currentBatch = it
            })



        checkForCheckedChips()

        //Months according to Chips checked
        binding.chipGroup.children.forEach {
            (it as Chip).setOnCheckedChangeListener{ buttonView, isChecked ->

             if (it.isChecked) {

                 val c = Calendar.getInstance()
                 val year = c.get(Calendar.YEAR)
                 val month = c.get(Calendar.MONTH)
                 val day = c.get(Calendar.DAY_OF_MONTH)

                 val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->


                     // Display Selected date
                     val date = "" + dayOfMonth + " " + MONTHS[monthOfYear] + ", " + year
                     Log.d("Day saved", date)

                 }, year, month, day)

                 dpd.show()

             }
            }
        }






        binding.studentNameField.editText?.setText(args.currentStudent.studentName)
        binding.contactNumberField.editText?.setText(args.currentStudent.studentNumber.toString())
        binding.feesField.editText?.setText(args.currentStudent.feesAmount.toString())
        binding.emailField.editText?.setText(args.currentStudent.studentEmail)
        binding.batchNameStudent.text = args.currentStudent.batchName
        binding.autoCompleteTextView.setText(args.currentStudent.feesStatus.toString(), false)


        var position = 0
        position = when (args.currentStudent.feesStatus) {
            FeeStatus.None -> {
                0
            }
            FeeStatus.Paid -> {
                1
            }
            FeeStatus.Unpaid -> {
                3
            }
        }

        binding.autoCompleteTextView.listSelection = position
        feeStatusSaved = binding.autoCompleteTextView.text.toString()
        binding.autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val selection = parent.getItemAtPosition(position) as String
                 feeStatusSaved = selection
            }






        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_student_menu, menu)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.update_student) {

            updateStudentToDb()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkForCheckedChips() {

        val stringRetrieved = args.currentStudent.monthsPaid
        Log.d("Show", "Tags $stringRetrieved")


           if(stringRetrieved.contains("January", true)) {
                binding.janChip.isChecked = true
            }
            if( stringRetrieved.contains("February",true)) {
                binding.febChip.isChecked = true
            }
            if (stringRetrieved.contains("March",true)) {
                binding.marChip.isChecked = true
            }
            if (stringRetrieved.contains("April",true)) {
                binding.aprChip.isChecked = true
            }
            if (stringRetrieved.contains("May",true)) {
                binding.mayChip.isChecked = true
            }
            if (stringRetrieved.contains("June",true)) {
                binding.juneChip.isChecked = true
            }
            if (stringRetrieved.contains("July",true)) {
                binding.julChip.isChecked = true
            }
            if(stringRetrieved.contains("August",true)) {
                binding.augChip.isChecked = true
            }
            if (stringRetrieved.contains("September",true))  {
                binding.sepChip.isChecked = true
            }
            if (stringRetrieved.contains("October",true))  {
                binding.octChip.isChecked = true
            }
            if (stringRetrieved.contains("November",true))  {
                binding.novChip.isChecked = true
            }
            if (stringRetrieved.contains("December",true))  {
                binding.decChip.isChecked = true
            }



    }










    private fun updateStudentToDb() {

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

                else -> {stringMonths.append("Empty")}

            }
        }




        monthsSaved = stringMonths.toString()

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
                batchName = args.currentStudent.batchName,
                monthsPaid = monthsSaved

            )

            studentViewModel.updateStudent(newStudentData)
            Toast.makeText(requireContext(), "Successfully updated student!", Toast.LENGTH_SHORT)
                .show()

            val action =
                UpdateFragmentDirections.actionUpdateFragmentToStudentListFragment(currentBatch)
            findNavController().navigate(action)

        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT)
                .show()
        }


    }

    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
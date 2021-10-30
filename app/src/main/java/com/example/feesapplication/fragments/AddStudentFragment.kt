package com.example.feesapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.feesapplication.R
import com.example.feesapplication.databinding.AddStudentFragmentBinding
import android.widget.AdapterView.OnItemClickListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.feesapplication.StudentViewModel
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.data.viewmodel.SharedViewModel


class AddStudentFragment : Fragment() {

    private val studentViewModel : StudentViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by viewModels()

    private lateinit var feeStatusSaved : String

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
        binding.lifecycleOwner = this




        // Set Menu
        setHasOptionsMenu(true)
        binding.autoCompleteTextView.onItemClickListener =
            OnItemClickListener { parent, view, position, rowId ->
                val selection = parent.getItemAtPosition(position) as String
                feeStatusSaved = selection
                Log.d("FOund", feeStatusSaved)
            }

        return binding.root
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_student_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
       if(item.itemId == R.id.save_student) {

           insertStudentToDb()

       }
        return super.onOptionsItemSelected(item)
    }

    private fun insertStudentToDb() {

        val studentNameField = binding.studentNameField.editText?.text.toString()
        val contactNumber = binding.contactNumberField.editText?.text.toString()
        val feesField = binding.feesField.editText?.text.toString()
        val emailField = binding.emailField.editText?.text.toString()
        val batchName = ""


        val validation = sharedViewModel.verifyStudentInputData(studentNameField, contactNumber, feesField, feeStatusSaved, emailField)
        if (validation) {
            // Green signal to save data into database
            val newStudentData = Student(
                studentName = studentNameField,
                studentNumber = contactNumber.toLong(),
                feesAmount = feesField.toDouble(),
                feesStatus = sharedViewModel.parseFeesStatus(feeStatusSaved),
                studentEmail = emailField,
                batchName = " "// FIGURE OUT

            )

            studentViewModel.insertStudent(newStudentData)
            Toast.makeText(requireContext(),"Successfully added student!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addStudentFragment2_to_studentListFragment)
        } else {
            Toast.makeText(requireContext(),"Please fill out all fields.", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





}
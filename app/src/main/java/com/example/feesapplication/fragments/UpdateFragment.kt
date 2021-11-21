package com.example.feesapplication.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavGraph
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.feesapplication.R
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.data.viewmodel.SharedViewModel
import com.example.feesapplication.data.viewmodel.StudentViewModel
import com.example.feesapplication.databinding.FragmentUpdateBinding


class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    private val studentViewModel : StudentViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by viewModels()

    private var feeStatusSaved = "None"
    private lateinit var currentStudent: Student
    private lateinit var currentBatch: Batch


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
        studentViewModel.getBatchOfStudent(args.currentStudent.batchName).observe(viewLifecycleOwner, {
            currentBatch = it
        })






        binding.studentNameField.editText?.setText(args.currentStudent.studentName)
        binding.contactNumberField.editText?.setText(args.currentStudent.studentNumber.toString())
        binding.feesField.editText?.setText(args.currentStudent.feesAmount.toString())
        binding.emailField.editText?.setText(args.currentStudent.studentEmail)
        binding.batchNameStudent.text = args.currentStudent.batchName
        binding.autoCompleteTextView.setText(args.currentStudent.feesStatus.toString(), false)




        binding.autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, rowId ->
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
        if(item.itemId == R.id.update_student) {

            updateStudentToDb()

        }
        return super.onOptionsItemSelected(item)
    }


    private fun updateStudentToDb() {

        val studentNameField = binding.studentNameField.editText?.text.toString()
        val contactNumber = binding.contactNumberField.editText?.text.toString()
        val feesField = binding.feesField.editText?.text.toString()
        val emailField = binding.emailField.editText?.text.toString()


        val validation = sharedViewModel.verifyStudentInputData(studentNameField, contactNumber, feesField, feeStatusSaved, emailField)
        if (validation) {
            // Green signal to save data into database
            val newStudentData = Student(
                studentName = studentNameField,
                studentNumber = contactNumber.toLong(),
                feesAmount = feesField.toDouble(),
                feesStatus = sharedViewModel.parseFeesStatus(feeStatusSaved),
                studentEmail = emailField,
                batchName = args.currentStudent.batchName

            )

            studentViewModel.updateStudent(newStudentData)
            Toast.makeText(requireContext(),"Successfully updated student!", Toast.LENGTH_SHORT).show()

            val action = UpdateFragmentDirections.actionUpdateFragmentToStudentListFragment(currentBatch)
            findNavController().navigate(action)

        /*    val navOptions = NavOptions
                .Builder()
                .setEnterAnim(R.anim.from_left)
                .setExitAnim(R.anim.to_right)
                .setPopEnterAnim(R.anim.from_left)
                .setPopExitAnim(R.anim.to_right)
                .setPopUpTo(R.id.studentListFragment, true)
                .build()




            findNavController().navigate(action, navOptions) */
        } else {
            Toast.makeText(requireContext(),"Please fill out all fields.", Toast.LENGTH_SHORT).show()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}
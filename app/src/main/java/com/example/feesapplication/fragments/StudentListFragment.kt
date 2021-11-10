package com.example.feesapplication.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feesapplication.R
import com.example.feesapplication.StudentViewModel
import com.example.feesapplication.data.viewmodel.SharedViewModel
import com.example.feesapplication.databinding.StudentListFragmentBinding
import com.example.feesapplication.list.StudentListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class StudentListFragment : Fragment() {


    private lateinit var batchNameSaved: String

    private val args by navArgs<StudentListFragmentArgs>()


    private val studentViewModel: StudentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private val studentListAdapter: StudentListAdapter by lazy { StudentListAdapter() }


    private var _binding: StudentListFragmentBinding? = null
    private val binding get() = _binding!!





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //No Data Views Visible/Invisible

        //Data Binding
        _binding = StudentListFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

        batchNameSaved = args.currentBatch.batchName

        setupRecyclerView()


        studentViewModel.studentsInBatch(batchNameSaved).observe(viewLifecycleOwner, Observer {
                data ->
            sharedViewModel.checkIfStudentDatabaseEmpty(data)
            studentListAdapter.setStudentData(data)
        })

        sharedViewModel.emptyStudentDatabase.observe(viewLifecycleOwner, Observer { showViews(it) })




        //Set Menu
        setHasOptionsMenu(true)


        return binding.root
    }

    private fun showViews(emptyBatchDatabase : Boolean) {
        if(emptyBatchDatabase) {
            binding.noStudentsImageView.visibility = View.VISIBLE
            binding.noStudentsTextview.visibility = View.VISIBLE
        } else {
            binding.noStudentsImageView.visibility = View.INVISIBLE
            binding.noStudentsTextview.visibility = View.INVISIBLE
        }
    }


    private fun setupRecyclerView() {
        //Setup RecyclerView
        val recyclerView = binding.studentRecyclerView
        recyclerView.adapter = studentListAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.student_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.add_student) {
        val action = StudentListFragmentDirections.actionStudentListFragmentToAddStudentFragment2(args.currentBatch)
        findNavController().navigate(action) }

        else if (item.itemId == R.id.delete_all) {
            confirmStudentsRemoval()
        }





        return super.onOptionsItemSelected(item)
    }

    private fun confirmStudentsRemoval() {
        val builder = MaterialAlertDialogBuilder(requireContext())


        builder.setPositiveButton("Yes") {_,_ ->
            studentViewModel.deleteAllStudents(batchNameSaved)
            Toast.makeText((
                    requireContext()),
                "Sucessfully Removed all Students from $batchNameSaved",
                Toast.LENGTH_SHORT
                    ).show()
        }

        builder.setNegativeButton("No") { _,_ -> }
        builder.setTitle("Delete students from $batchNameSaved?")
        builder.setMessage("Are you sure you want to remove all students from ${batchNameSaved}?")
        builder.create().show()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
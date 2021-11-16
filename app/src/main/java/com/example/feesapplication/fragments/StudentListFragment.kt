package com.example.feesapplication.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feesapplication.R
import com.example.feesapplication.data.viewmodel.StudentViewModel
import com.example.feesapplication.data.viewmodel.SharedViewModel
import com.example.feesapplication.databinding.StudentListFragmentBinding
import com.example.feesapplication.adapters.StudentListAdapter
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.list.SwipeToDelete
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar



class StudentListFragment : Fragment(), SearchView.OnQueryTextListener {


    private lateinit var batchNameSaved: String
    private lateinit var foundQuery : String


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


        //Data Binding
        _binding = StudentListFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

        batchNameSaved = args.currentBatch?.batchName.toString()
        binding.studentRecyclerView.scheduleLayoutAnimation()

        // Set up RecyclerView
        setupRecyclerView()

        // Observe Live Data and Populate Student List
        populateRecyclerView()








        //Set Menu
        setHasOptionsMenu(true)


        return binding.root
    }


    fun populateRecyclerView() {
        if (args.currentBatch != null) {

            // Get all students in batch

            studentViewModel.studentsInBatch(batchNameSaved).observe(viewLifecycleOwner, Observer {
                    data ->
                sharedViewModel.checkIfStudentDatabaseEmpty(data)
                studentListAdapter.setStudentData(data)
                binding.studentRecyclerView.scheduleLayoutAnimation()
            })
        } else if (args.currentBatch == null) {

            // Get all students in Database

            studentViewModel.getAllStudentData.observe(viewLifecycleOwner, Observer { data ->
                studentListAdapter.setStudentData(data)
                binding.studentRecyclerView.scheduleLayoutAnimation()
                sharedViewModel.checkIfStudentDatabaseEmpty(data)

            })
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.student_list_menu, menu)

        val search = menu.findItem(R.id.student_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)

       if (args.currentBatch == null) {
           menu.findItem(R.id.add_student).isVisible = false
           menu.findItem(R.id.delete_all).isVisible = false
           menu.findItem(R.id.search_all_students).isVisible = false


       }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (args.currentBatch != null) {

            if (item.itemId == R.id.add_student) {
                val action = args.currentBatch?.let {
                    StudentListFragmentDirections.actionStudentListFragmentToAddStudentFragment2(
                        it
                    )
                }
                action?.let { findNavController().navigate(it) }
            } else if (item.itemId == R.id.delete_all) {
                confirmStudentsRemoval()
            } else if (item.itemId == R.id.search_all_students) {

                findNavController().navigate(R.id.action_studentListFragment_self)
                populateRecyclerView()

            }
        }

         if (item.itemId == R.id.menu_paid_fees) {
             studentViewModel.sortByPaid.observe(this, Observer {studentListAdapter.setStudentData(it)})
        } else if (item.itemId == R.id.menu_unpaid_fees) {
            studentViewModel.sortByUnpaid.observe(this, Observer { studentListAdapter.setStudentData(it)})
         }






        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null) {
            searchInStudentDatabase(query)
        }
        return true
    }


    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null) {
            searchInStudentDatabase(query)
        }
        return true
    }

    private fun searchInStudentDatabase(query: String) {
        val searchQuery = "%$query%"


        if (args.currentBatch != null) {
            studentViewModel.searchStudentInBatch(batchNameSaved, searchQuery)
                .observe(viewLifecycleOwner, Observer { list ->
                    list?.let {
                        studentListAdapter.setStudentData(it)

                    }
                })
        } else {

            studentViewModel.searchStudentDatabase(searchQuery).observe(this, Observer { list ->
                list?.let {
                    studentListAdapter.setStudentData(it)
                }
            })
        }


    }




    private fun setupRecyclerView() {
        //Setup RecyclerView
        val recyclerView = binding.studentRecyclerView
        recyclerView.adapter = studentListAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.studentRecyclerView.scheduleLayoutAnimation()

        // Swipe TO Delete
        swipeToDelete(recyclerView)
    }

     private fun swipeToDelete(studentRecyclerView: RecyclerView) {
         val swipeToDeleteCallback = object : SwipeToDelete() {
             override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                 val deletedItem = studentListAdapter.studentList[viewHolder.bindingAdapterPosition]
                 // Delete Student

                 studentViewModel.deleteStudent(deletedItem)
                 studentListAdapter.notifyItemRemoved(viewHolder.bindingAdapterPosition)

                 // Restore Deleted Student
                 restoreDeletedStudent(viewHolder.itemView, deletedItem, viewHolder.bindingAdapterPosition)
             }
         }
         val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
         itemTouchHelper.attachToRecyclerView(studentRecyclerView)

     }

    private fun restoreDeletedStudent(view: View, deletedItem: Student, position: Int) {
        val snackBar = Snackbar.make(
            view, "Deleted record of '${deletedItem.studentName}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo"){
            studentViewModel.insertStudent(deletedItem)
            studentListAdapter.notifyItemChanged(position)
        }
        snackBar.show()
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
package com.example.feesapplication.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.feesapplication.R
import com.example.feesapplication.adapters.StudentListAdapter
import com.example.feesapplication.data.database.FeeStatus
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.data.viewmodel.SharedViewModel
import com.example.feesapplication.data.viewmodel.StudentViewModel
import com.example.feesapplication.databinding.StudentListFragmentBinding
import com.example.feesapplication.list.SwipeToDelete
import com.example.feesapplication.list.utils.hideKeyboard
import com.example.feesapplication.list.utils.observeOnce
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


class StudentListFragment : Fragment(), SearchView.OnQueryTextListener {


    lateinit var batchNameSaved: String


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
    ): View {


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

        //Hide Keyboard
        hideKeyboard(requireActivity())




        return binding.root
    }


    private fun populateRecyclerView() {
        if (args.currentBatch != null) {

            // Get all students in batch

            studentViewModel.studentsInBatch(batchNameSaved)
                .observe(viewLifecycleOwner, { data ->
                    studentListAdapter.setStudentData(data)
                    sharedViewModel.checkIfStudentDatabaseEmpty(data)
                    binding.studentRecyclerView.scheduleLayoutAnimation()

                })
        } else if (args.currentBatch == null) {

            // Get all students in Database

            studentViewModel.getAllStudentData.observe(viewLifecycleOwner, { data ->
                studentListAdapter.setStudentData(data)
                sharedViewModel.checkIfStudentDatabaseEmpty(data)
                binding.studentRecyclerView.scheduleLayoutAnimation()

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
            menu.findItem(R.id.reset_batch).isVisible = false


        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        if (args.currentBatch != null) {

            when (item.itemId) {
                R.id.add_student -> {
                    val action = args.currentBatch?.let {
                        StudentListFragmentDirections.actionStudentListFragmentToAddStudentFragment2(
                            it
                        )
                    }
                    action?.let { findNavController().navigate(it) }
                }
                R.id.reset_batch -> {

                    val builder = MaterialAlertDialogBuilder(requireActivity())


                    builder.setPositiveButton("Yes") { _, _ ->

                        studentViewModel.studentsInBatch(batchNameSaved).observeOnce(
                            viewLifecycleOwner,
                            { student ->
                                for (i in student.indices) {

                                    val updatedStudentData = Student(
                                        studentName = student[i].studentName,
                                        studentNumber = student[i].studentNumber,
                                        feesAmount = student[i].feesAmount,
                                        feesStatus = FeeStatus.Unpaid,
                                        studentEmail = student[i].studentEmail,
                                        batchName = student[i].batchName,
                                        monthsPaid = student[i].monthsPaid

                                    )

                                    studentViewModel.updateStudent(updatedStudentData)

                                }

                            })

                        Toast.makeText(
                            (requireContext()),
                            "Successfully reset batch to Unpaid",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    builder.setNegativeButton("No") { _, _ -> }
                    builder.setTitle("Reset to Unpaid?")
                    builder.setIcon(R.drawable.ic_reset_unpaid)
                    builder.setMessage("Are you sure you want to reset batch records to Unpaid status?")
                    builder.create().show()

                }
                R.id.delete_all -> {
                    confirmStudentsRemoval()
                }

                R.id.search_all_students -> {

                    findNavController().navigate(R.id.action_studentListFragment_self)
                    populateRecyclerView()

                }
                R.id.mail_all_students -> {

                    val batchNameAction =
                        StudentListFragmentDirections.actionStudentListFragmentToEmail(
                            null,
                            batchNameSaved
                        )
                    findNavController().navigate(batchNameAction)

                }
                R.id.sms_all_students -> {


                    val smsBatchAction =
                        StudentListFragmentDirections.actionStudentListFragmentToSMS(
                            null,
                            batchNameSaved
                        )
                    findNavController().navigate(smsBatchAction)


                }
            }
        }

        if (item.itemId == R.id.menu_paid_fees) {


            if (args.currentBatch == null) {

                studentViewModel.sortByPaid.observeOnce(viewLifecycleOwner, {
                    studentListAdapter.setStudentData(it)
                    sharedViewModel.checkIfStudentDatabaseEmpty(it)
                    binding.studentRecyclerView.scheduleLayoutAnimation()
                })

            } else if (args.currentBatch != null) {

                studentViewModel.sortByBatchPaid(batchNameSaved)
                    .observeOnce(viewLifecycleOwner, {
                        studentListAdapter.setStudentData(it)
                        sharedViewModel.checkIfStudentDatabaseEmpty(it)
                        binding.studentRecyclerView.scheduleLayoutAnimation()
                    })

            }
        } else if (item.itemId == R.id.menu_unpaid_fees) {


            if (args.currentBatch == null) {

                studentViewModel.sortByUnpaid.observeOnce(viewLifecycleOwner, {
                    studentListAdapter.setStudentData(it)
                    sharedViewModel.checkIfStudentDatabaseEmpty(it)
                    binding.studentRecyclerView.scheduleLayoutAnimation()
                })

            } else if (args.currentBatch != null) {

                studentViewModel.sortByBatchUnpaid(batchNameSaved)
                    .observeOnce(viewLifecycleOwner, {
                        studentListAdapter.setStudentData(it)
                        sharedViewModel.checkIfStudentDatabaseEmpty(it)
                        binding.studentRecyclerView.scheduleLayoutAnimation()
                    })

            }

        }

        return super.onOptionsItemSelected(item)
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchInStudentDatabase(query)
        }
        return true
    }


    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchInStudentDatabase(query)
        }
        return true
    }

    private fun searchInStudentDatabase(query: String) {
        val searchQuery = "%$query%"


        if (args.currentBatch != null) {
            studentViewModel.searchStudentInBatch(batchNameSaved, searchQuery)
                .observeOnce(viewLifecycleOwner, { list ->
                    list?.let {
                        studentListAdapter.setStudentData(it)
                        sharedViewModel.checkIfStudentDatabaseEmpty(list)
                        binding.studentRecyclerView.scheduleLayoutAnimation()

                    }
                })
        } else if (args.currentBatch == null) {

            studentViewModel.searchStudentDatabase(searchQuery)
                .observeOnce(viewLifecycleOwner, { list ->
                    list?.let {
                        studentListAdapter.setStudentData(it)
                        sharedViewModel.checkIfStudentDatabaseEmpty(list)
                        binding.studentRecyclerView.scheduleLayoutAnimation()
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
                val builder = MaterialAlertDialogBuilder(requireContext())

                // Delete Student
                builder.setPositiveButton("Yes") { _, _ ->
                    studentViewModel.deleteStudent(deletedItem)
                    studentListAdapter.notifyItemRemoved(viewHolder.bindingAdapterPosition)
                    restoreDeletedStudent(viewHolder.itemView, deletedItem)
                }
                // Restore Deleted Student
                builder.setNegativeButton("No") { _, _ -> studentViewModel.insertStudent(deletedItem) }
                builder.setTitle("Delete record of ${deletedItem.studentName}?")
                builder.setMessage("Are you sure you want to remove ${deletedItem.studentName}?")
                builder.setIcon(R.drawable.ic_delete_student)
                builder.setCancelable(false)
                builder.create().show()
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(studentRecyclerView)

    }


    private fun restoreDeletedStudent(view: View, deletedItem: Student) {
        val snackBar = Snackbar.make(
            view, "Deleted record of '${deletedItem.studentName}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            studentViewModel.insertStudent(deletedItem)
        }
        snackBar.show()
    }


    private fun confirmStudentsRemoval() {
        val builder = MaterialAlertDialogBuilder(requireContext())


        builder.setPositiveButton("Yes") { _, _ ->
            studentViewModel.deleteAllStudents(batchNameSaved)
            Toast.makeText(
                (
                        requireContext()),
                "Successfully Removed all Students from $batchNameSaved",
                Toast.LENGTH_SHORT
            ).show()
        }

        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete students from $batchNameSaved?")
        builder.setMessage("Are you sure you want to remove all students from ${batchNameSaved}?")
        builder.create().show()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
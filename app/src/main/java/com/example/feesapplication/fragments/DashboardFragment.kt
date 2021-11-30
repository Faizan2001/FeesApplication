package com.example.feesapplication.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.feesapplication.R
import com.example.feesapplication.adapters.ListAdapter
import com.example.feesapplication.data.database.FeeStatus
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.data.viewmodel.SharedViewModel
import com.example.feesapplication.data.viewmodel.StudentViewModel
import com.example.feesapplication.databinding.DashboardFragmentBinding
import com.example.feesapplication.list.SwipeToDelete
import com.example.feesapplication.list.utils.hideKeyboard
import com.example.feesapplication.list.utils.observeOnce
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.util.*


class DashboardFragment : Fragment(), SearchView.OnQueryTextListener {

    private val studentViewModel: StudentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private val adapter: ListAdapter by lazy { ListAdapter() }

    private var _binding: DashboardFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayShowCustomEnabled(false)
        (activity as AppCompatActivity?)!!.supportActionBar?.setDisplayHomeAsUpEnabled(false)


        //Data Binding
        _binding = DashboardFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

        binding.recyclerView.scheduleLayoutAnimation()
        binding.imageViewBackground.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_reportFragment)
        }

        //Set scenery and text of Greeting Image
        setSceneryAndDataForTime()


        //Setup RecyclerView
        setupRecyclerView()


        // Observing Live Data Fetched
        studentViewModel.getAllBatchData.observe(viewLifecycleOwner, { data ->
            adapter.setData(data)
            binding.recyclerView.scheduleLayoutAnimation()
            sharedViewModel.checkIfBatchDatabaseEmpty(data)

        })


        //Set Menu
        setHasOptionsMenu(true)

        //Hide Keyboard
        hideKeyboard(requireActivity())


        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.dashboard_menu, menu)

        val search = menu.findItem(R.id.batch_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.reset_all -> {
                val builder = MaterialAlertDialogBuilder(requireActivity())


                builder.setPositiveButton("Yes") { _, _ ->

                    studentViewModel.getAllStudentData.observeOnce(
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

                }

                builder.setNegativeButton("No") { _, _ -> }
                builder.setTitle("Reset all?")
                builder.setIcon(R.drawable.ic_reset_unpaid)
                builder.setMessage("Are you sure you want to reset all records to Unpaid status?")
                builder.create().show()

            }
            R.id.showAllBatches -> {
                studentViewModel.getAllBatchData.observe(viewLifecycleOwner, { data ->
                    adapter.setData(data)
                    sharedViewModel.checkIfBatchDatabaseEmpty(data)
                    binding.recyclerView.scheduleLayoutAnimation()
                })
            }
            R.id.sunday_menu -> {
                studentViewModel.sortBySunday.observe(viewLifecycleOwner, {
                    adapter.setData(it)
                    sharedViewModel.checkIfBatchDatabaseEmpty(it)
                    binding.recyclerView.scheduleLayoutAnimation()
                })
            }

            R.id.monday_menu -> {
                studentViewModel.sortByMonday.observe(viewLifecycleOwner, {
                    adapter.setData(it)
                    sharedViewModel.checkIfBatchDatabaseEmpty(it)
                    binding.recyclerView.scheduleLayoutAnimation()
                })
            }

            R.id.tuesday_menu -> {
                studentViewModel.sortByTuesday.observe(viewLifecycleOwner, {
                    adapter.setData(it)
                    sharedViewModel.checkIfBatchDatabaseEmpty(it)
                    binding.recyclerView.scheduleLayoutAnimation()
                })
            }

            R.id.wednesday_menu -> {
                studentViewModel.sortByWednesday.observe(viewLifecycleOwner, {
                    adapter.setData(it)
                    sharedViewModel.checkIfBatchDatabaseEmpty(it)
                    binding.recyclerView.scheduleLayoutAnimation()
                })
            }

            R.id.thursday_menu -> {
                studentViewModel.sortByThursday.observe(viewLifecycleOwner, {
                    adapter.setData(it)
                    sharedViewModel.checkIfBatchDatabaseEmpty(it)
                    binding.recyclerView.scheduleLayoutAnimation()
                })
            }

            R.id.friday_menu -> {
                studentViewModel.sortByFriday.observe(viewLifecycleOwner, {
                    adapter.setData(it)
                    sharedViewModel.checkIfBatchDatabaseEmpty(it)
                    binding.recyclerView.scheduleLayoutAnimation()
                })
            }

            R.id.saturday_menu -> {
                studentViewModel.sortBySaturday.observe(viewLifecycleOwner, {
                    adapter.setData(it)
                    sharedViewModel.checkIfBatchDatabaseEmpty(it)
                    binding.recyclerView.scheduleLayoutAnimation()
                })
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.scheduleLayoutAnimation()


        // Swipe to Delete
        swipeToDelete(recyclerView)
    }

    private fun setSceneryAndDataForTime() {
        val c = Calendar.getInstance()

        when {
            c.get(Calendar.HOUR_OF_DAY) in 6..11 -> {
                binding.apply {
                    imageView4.setImageResource(R.drawable.castle_artmade)
                    titleText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.primaryDarkColor
                        )
                    )
                    bText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    sText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    reportViewText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    titleText.text = getString(R.string.good_m)
                }
                studentViewModel.getBatchCount.observe(viewLifecycleOwner, {
                    binding.bText.text = "Batches: $it"
                })
                studentViewModel.getStudentCount.observe(viewLifecycleOwner, {
                    binding.sText.text = "Students: $it"
                })
            }
            c.get(Calendar.HOUR_OF_DAY) in 12..15 -> {
                binding.apply {
                    imageView4.setImageResource(R.drawable.castle_artmade)
                    titleText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.primaryDarkColor
                        )
                    )
                    bText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    sText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    reportViewText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    titleText.text = "Good Afternoon"
                }
                studentViewModel.getBatchCount.observe(viewLifecycleOwner, {
                    binding.bText.text = "Batches: $it"
                })
                studentViewModel.getStudentCount.observe(viewLifecycleOwner, {
                    binding.sText.text = "Students: $it"
                })

            }
            c.get(Calendar.HOUR_OF_DAY) in 16..18 -> {
                binding.apply {
                    imageView4.setImageResource(R.drawable.castle_evening)
                    titleText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.primaryDarkColor
                        )
                    )
                    bText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    sText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    reportViewText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    titleText.text = "Good Evening"
                }
                studentViewModel.getBatchCount.observe(viewLifecycleOwner, {
                    binding.bText.text = "Batches: $it"
                })
                studentViewModel.getStudentCount.observe(viewLifecycleOwner, {
                    binding.sText.text = "Students: $it"
                })
            }
            c.get(Calendar.HOUR_OF_DAY) in 19..23 -> {
                binding.apply {
                    imageView4.setImageResource(R.drawable.castle_night)
                    titleText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    bText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    sText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    reportViewText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    titleText.text = "Good Evening"
                }
                studentViewModel.getBatchCount.observe(viewLifecycleOwner, {
                    binding.bText.text = "Batches: $it"
                })
                studentViewModel.getStudentCount.observe(viewLifecycleOwner, {
                    binding.sText.text = "Students: $it"
                })
            }
            c.get(Calendar.HOUR_OF_DAY) in 0..5 -> {
                binding.apply {
                    imageView4.setImageResource(R.drawable.castle_night)
                    titleText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    bText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    sText.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    reportViewText.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.white
                        )
                    )
                    titleText.text = "Hello"
                }
                studentViewModel.getBatchCount.observe(viewLifecycleOwner, {
                    binding.bText.text = "Batches: $it"
                })
                studentViewModel.getStudentCount.observe(viewLifecycleOwner, {
                    binding.sText.text = "Students: $it"
                })
            }
        }

    }

    private fun swipeToDelete(batchRecyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val deletedItem = adapter.batchList[viewHolder.bindingAdapterPosition]
                val builder = MaterialAlertDialogBuilder(requireContext())

                //Delete Student

                builder.setPositiveButton("Yes") { _, _ ->
                    studentViewModel.deleteBatch(deletedItem)
                    studentViewModel.deleteAllStudents(deletedItem.batchName)
                    adapter.notifyItemRemoved(viewHolder.bindingAdapterPosition)
                    restoreDeletedBatch(viewHolder.itemView, deletedItem)
                }

                // Restore Deleted Student
                builder.setNegativeButton("No") { _, _ -> studentViewModel.insertBatch(deletedItem) }
                builder.setTitle("Delete ${deletedItem.batchName}?")
                builder.setMessage("Are you sure you want to remove ${deletedItem.batchName} and all of its contents?")
                builder.setIcon(R.drawable.ic_batch_delete)
                builder.setCancelable(false)
                builder.create().show()

            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(batchRecyclerView)

    }

    private fun restoreDeletedBatch(view: View, deletedItem: Batch) {
        val snackBar = Snackbar.make(
            view, "Deleted batch '${deletedItem.batchName}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            studentViewModel.insertBatch(deletedItem)
        }
        snackBar.show()
    }


    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchInDatabase(query)
        }
        return true
    }


    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchInDatabase(query)
        }
        return true
    }

    private fun searchInDatabase(query: String) {
        val searchQuery = "%$query%"

        studentViewModel.searchBatchDatabase(searchQuery)
            .observeOnce(viewLifecycleOwner, { list ->
                list?.let {
                    adapter.setData(it)
                    sharedViewModel.checkIfBatchDatabaseEmpty(list)
                    binding.recyclerView.scheduleLayoutAnimation()
                }
            })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}





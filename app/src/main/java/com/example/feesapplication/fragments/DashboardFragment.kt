package com.example.feesapplication.fragments

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.*
import com.example.feesapplication.R
import com.example.feesapplication.data.viewmodel.StudentViewModel
import com.example.feesapplication.data.viewmodel.SharedViewModel
import com.example.feesapplication.databinding.DashboardFragmentBinding
import com.example.feesapplication.adapters.ListAdapter
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.list.SwipeToDelete
import com.example.feesapplication.list.utils.hideKeyboard
import com.example.feesapplication.list.utils.observeOnce
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.io.*
import java.lang.Exception

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

        //Data Binding
        _binding = DashboardFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

        binding.recyclerView.scheduleLayoutAnimation()


        //Setup RecyclerView
       setupRecyclerView()

        // Observing Live Data Fetched
        studentViewModel.getAllBatchData.observe(viewLifecycleOwner, Observer { data ->
            adapter.setData(data)
            binding.recyclerView.scheduleLayoutAnimation()
            sharedViewModel.checkIfBatchDatabaseEmpty(data)
        })




        //Set Menu
        setHasOptionsMenu(true)

        //Hide Keyboard
        hideKeyboard(requireActivity())
        saveReportInDeviceStorage()

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
            R.id.showAllBatches -> {
                studentViewModel.getAllBatchData.observe(viewLifecycleOwner, Observer { data ->
                    adapter.setData(data)
                    sharedViewModel.checkIfBatchDatabaseEmpty(data)
                    binding.recyclerView.scheduleLayoutAnimation()
                })
            }
            R.id.sunday_menu -> {studentViewModel.sortBySunday.observe(viewLifecycleOwner, Observer { adapter.setData(it) })
                binding.recyclerView.scheduleLayoutAnimation()  }
            R.id.monday_menu -> { studentViewModel.sortByMonday.observe(viewLifecycleOwner, Observer { adapter.setData(it) })
            binding.recyclerView.scheduleLayoutAnimation()  }
            R.id.tuesday_menu -> { studentViewModel.sortByTuesday.observe(viewLifecycleOwner, Observer { adapter.setData(it) })
             binding.recyclerView.scheduleLayoutAnimation()  }
            R.id.wednesday_menu -> { studentViewModel.sortByWednesday.observe(viewLifecycleOwner, Observer { adapter.setData(it) })
             binding.recyclerView.scheduleLayoutAnimation()  }
            R.id.thursday_menu -> { studentViewModel.sortByThursday.observe(viewLifecycleOwner, Observer { adapter.setData(it) })
            binding.recyclerView.scheduleLayoutAnimation()  }
            R.id.friday_menu -> { studentViewModel.sortByFriday.observe(viewLifecycleOwner, Observer { adapter.setData(it) })
            binding.recyclerView.scheduleLayoutAnimation()  }
            R.id.saturday_menu -> { studentViewModel.sortBySaturday.observe(viewLifecycleOwner, Observer { adapter.setData(it) })
            binding.recyclerView.scheduleLayoutAnimation()  }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.scheduleLayoutAnimation()


        // Swipe to Delete
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(batchRecyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val deletedItem = adapter.batchList[viewHolder.bindingAdapterPosition]
                val builder = MaterialAlertDialogBuilder(requireContext())

                //Delete Student

                builder.setPositiveButton("Yes") {_,_ ->
                    studentViewModel.deleteBatch(deletedItem)
                    studentViewModel.deleteAllStudents(deletedItem.batchName)
                    adapter.notifyItemRemoved(viewHolder.bindingAdapterPosition)
                    restoreDeletedBatch(viewHolder.itemView, deletedItem)
                }

                // Restore Deleted Student
                builder.setNegativeButton("No") { _,_ ->  studentViewModel.insertBatch(deletedItem) }
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
        snackBar.setAction("Undo"){
            studentViewModel.insertBatch(deletedItem)
        }
        snackBar.show()
    }



    override fun onQueryTextSubmit(query: String?): Boolean {
       if(query != null) {
           searchInDatabase(query)
       }
        return true
    }


    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null) {
            searchInDatabase(query)
        }
        return true
    }

    private fun searchInDatabase(query: String) {
        val searchQuery = "%$query%"

        studentViewModel.searchBatchDatabase(searchQuery).observeOnce(viewLifecycleOwner, Observer { list ->
            list?.let {
                adapter.setData(it)
                binding.recyclerView.scheduleLayoutAnimation()
            }
        })

    }

    private fun saveReportInDeviceStorage() {
        Log.d("WORKING", "YES")
        val HEADER = "DATE,NAME,AMOUNT_DUE,AMOUNT_PAID"

        var filename = "export.txt"

        var path = context?.getExternalFilesDir(null)   //get file directory for this package
        //(Android/data/.../files | ... is your app package)

        //create fileOut object
        var fileOut = File(path, filename)

        //delete any file object with path and filename that already exists
        fileOut.delete()

        //create a new file
        fileOut.createNewFile()

        //append the header and a newline
        fileOut.appendText(HEADER)
        fileOut.appendText("\n")


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}





package com.example.feesapplication.fragments

import android.os.Bundle
import android.view.*
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
import com.google.android.material.snackbar.Snackbar

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
    ): View? {

        //Data Binding
        _binding = DashboardFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel


        //Setup RecyclerView
       setupRecyclerView()

        // Observing Live Data Fetched
        studentViewModel.getAllBatchData.observe(viewLifecycleOwner, Observer { data ->
            adapter.setData(data)
            sharedViewModel.checkIfBatchDatabaseEmpty(data)
            binding.recyclerView.scheduleLayoutAnimation()
        })




        //Set Menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
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
            R.id.sunday_menu -> {studentViewModel.sortBySunday.observe(this, Observer { adapter.setData(it) })
                binding.recyclerView.scheduleLayoutAnimation()  }
            R.id.monday_menu -> { studentViewModel.sortByMonday.observe(this, Observer { adapter.setData(it) })
            binding.recyclerView.scheduleLayoutAnimation()  }
            R.id.tuesday_menu -> { studentViewModel.sortByTuesday.observe(this, Observer { adapter.setData(it) })
             binding.recyclerView.scheduleLayoutAnimation()  }
            R.id.wednesday_menu -> { studentViewModel.sortByWednesday.observe(this, Observer { adapter.setData(it) })
             binding.recyclerView.scheduleLayoutAnimation()  }
            R.id.thursday_menu -> { studentViewModel.sortByThursday.observe(this, Observer { adapter.setData(it) })
            binding.recyclerView.scheduleLayoutAnimation()  }
            R.id.friday_menu -> { studentViewModel.sortByFriday.observe(this, Observer { adapter.setData(it) })
            binding.recyclerView.scheduleLayoutAnimation()  }
            R.id.saturday_menu -> { studentViewModel.sortBySaturday.observe(this, Observer { adapter.setData(it) })
            binding.recyclerView.scheduleLayoutAnimation()  }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)


        // Swipe to Delete
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(batchRecyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.batchList[viewHolder.bindingAdapterPosition]
                //Delete Student

                studentViewModel.deleteBatch(deletedItem)
                adapter.notifyItemRemoved(viewHolder.bindingAdapterPosition)

                // Restore Deleted Student
                restoreDeletedBatch(viewHolder.itemView, deletedItem)

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

        studentViewModel.searchBatchDatabase(searchQuery).observe(this, Observer { list ->
            list?.let {
                adapter.setData(it)
            }
        })

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}





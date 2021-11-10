package com.example.feesapplication.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.feesapplication.R
import com.example.feesapplication.StudentViewModel
import com.example.feesapplication.data.viewmodel.SharedViewModel
import com.example.feesapplication.databinding.DashboardFragmentBinding
import com.example.feesapplication.list.ListAdapter

class DashboardFragment : Fragment() {

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
        
        studentViewModel.getAllBatchData.observe(viewLifecycleOwner, Observer { data ->
            adapter.setData(data)
            sharedViewModel.checkIfBatchDatabaseEmpty(data)
        })

        sharedViewModel.emptyBatchDatabase.observe(viewLifecycleOwner, Observer { showViews(it)})






        //Set Menu
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun showViews(emptyBatchDatabase : Boolean) {
        if(emptyBatchDatabase) {
            binding.noDataImageView.visibility = View.VISIBLE
            binding.noDataTextView.visibility = View.VISIBLE
        } else {
            binding.noDataImageView.visibility = View.INVISIBLE
            binding.noDataTextView.visibility = View.INVISIBLE
        }
    }

    private fun setupRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.dashboard_menu, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}





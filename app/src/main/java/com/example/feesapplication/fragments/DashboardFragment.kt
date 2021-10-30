package com.example.feesapplication.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
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


        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        
        studentViewModel.getAllBatchData.observe(viewLifecycleOwner, Observer {
            data -> adapter.setData(data)
        })

        binding.button.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_addStudentFragment2)
        }

        binding.button2.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_studentListFragment)
        }





        //Set Menu
        setHasOptionsMenu(true)

        return binding.root
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





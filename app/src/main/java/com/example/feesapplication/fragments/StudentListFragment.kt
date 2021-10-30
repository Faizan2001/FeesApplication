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
import com.example.feesapplication.databinding.StudentListFragmentBinding
import com.example.feesapplication.list.StudentListAdapter


class StudentListFragment : Fragment() {


    private val studentViewModel: StudentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private val adapter: StudentListAdapter by lazy { StudentListAdapter() }

    private var _binding: StudentListFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Data Binding
        _binding = StudentListFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel


        val recyclerView = binding.studentRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        studentViewModel.getAllStudentData.observe(viewLifecycleOwner, Observer {
                data -> adapter.setStudentData(data)
        })





        //Set Menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.student_list_menu, menu)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
package com.example.feesapplication.fragments

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
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


class StudentListFragment : Fragment() {


    private lateinit var batchNameSaved: String

    private val args by navArgs<StudentListFragmentArgs>()

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

        batchNameSaved = args.currentBatch.batchName


        //Setup RecyclerView
        setupRecyclerView()


        //Send AND observe data
        studentViewModel.studentsInBatch(batchNameSaved).observe(viewLifecycleOwner, {
                data -> adapter.setStudentData(data)
        })

        // Set Batch Name as Title








        //Set Menu
        setHasOptionsMenu(true)

    }

    private fun setupRecyclerView() {
        val recyclerView = binding.studentRecyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.student_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val action = StudentListFragmentDirections.actionStudentListFragmentToAddStudentFragment2(batchNameSaved)
        findNavController().navigate(action)

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
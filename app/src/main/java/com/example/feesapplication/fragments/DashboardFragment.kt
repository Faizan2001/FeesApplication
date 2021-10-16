package com.example.feesapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.feesapplication.StudentApplication
import com.example.feesapplication.StudentViewModel
import com.example.feesapplication.StudentViewModelFactory
import com.example.feesapplication.databinding.DashboardFragmentBinding

class DashboardFragment : Fragment() {

    private val viewModel: StudentViewModel by activityViewModels{
        StudentViewModelFactory(
            (activity?.application as StudentApplication).database.studentDao()
        )
    }

    private var _binding: DashboardFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DashboardFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      //  binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
    }

}
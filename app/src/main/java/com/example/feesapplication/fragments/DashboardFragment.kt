package com.example.feesapplication.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.feesapplication.R
import com.example.feesapplication.databinding.DashboardFragmentBinding

class DashboardFragment : Fragment() {

    private var _binding: DashboardFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DashboardFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.dashboardLayout.setOnClickListener {
        findNavController().navigate(R.id.action_dashboardFragment_to_studentListFragment) }


        //Set Menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.dashboard_menu, menu)
    }

}





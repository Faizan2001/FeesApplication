package com.example.feesapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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






        return binding.root
    }

}





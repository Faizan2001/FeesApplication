package com.example.feesapplication.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.feesapplication.R
import com.example.feesapplication.databinding.ReportFragmentBinding
import com.example.feesapplication.databinding.StudentListFragmentBinding
import com.example.feesapplication.list.utils.hideKeyboard


class ReportFragment : Fragment() {

    private var _binding: ReportFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = ReportFragmentBinding.inflate(inflater, container, false)


        //Set Menu
        setHasOptionsMenu(true)

        //Hide Keyboard
        hideKeyboard(requireActivity())
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.report_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)

        when(item.itemId) {
            R.id.save_report -> {}
            R.id.upload_report -> {}

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}
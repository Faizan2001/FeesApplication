package com.example.feesapplication.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.feesapplication.R


class AddStudentFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Set Menu
        setHasOptionsMenu(true)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_student_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_student_menu, menu)
    }


}
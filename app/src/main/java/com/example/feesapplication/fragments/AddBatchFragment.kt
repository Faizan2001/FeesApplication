package com.example.feesapplication.fragments

import android.content.ContentValues.TAG

import android.os.Bundle

import android.text.format.DateFormat.is24HourFormat
import android.view.*

import androidx.fragment.app.Fragment
import com.example.feesapplication.DayPickerDialogFragment
import com.example.feesapplication.R
import com.example.feesapplication.databinding.AddBatchFragmentBinding

import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat



class AddBatchFragment : Fragment() {




    private var _binding: AddBatchFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Set Menu
        setHasOptionsMenu(true)


        // Inflate the layout for this fragment
        _binding = AddBatchFragmentBinding.inflate(layoutInflater, container, false)

        binding.timePickerButton.setOnClickListener {
            openTimePicker()
        }

        binding.tvDay.setOnClickListener { alertDialog() }





        return binding.root

    }

    // TIME PICKER

    private fun openTimePicker() {
        val isSystem24Hour = is24HourFormat(requireContext())
        val clockFormat = if(isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .setTitleText("Batch Start Time")
            .build()

        picker.show(childFragmentManager, "TAG")

        picker.addOnPositiveButtonClickListener {
            val h = picker.hour
            val min = picker.minute
            var newTime : String

            if (picker.minute < 10) {
                newTime =  h.toString().plus(":0").plus(min.toString())
                binding.startTimeField.setText(newTime)
            } else {
                newTime = "$h:$min"
                binding.startTimeField.setText(newTime)
            }



        }
    }

    // DAY SELECTOR

   fun alertDialog() {

       DayPickerDialogFragment().show(
           childFragmentManager, TAG
       )

   }






    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.add_batch_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.save_batch) {

            insertDataToDb()

        }

        return super.onOptionsItemSelected(item)
    }


    private fun insertDataToDb() {

    }


}
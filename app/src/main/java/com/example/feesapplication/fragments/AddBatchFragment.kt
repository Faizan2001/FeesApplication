package com.example.feesapplication.fragments

import android.content.DialogInterface

import android.os.Bundle
import android.text.TextUtils

import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.*
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
// import com.example.feesapplication.DayPickerDialogFragment
import com.example.feesapplication.R
import com.example.feesapplication.StudentViewModel
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.data.viewmodel.SharedViewModel
import com.example.feesapplication.databinding.AddBatchFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList


class AddBatchFragment : Fragment(){

    private val studentViewModel : StudentViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by viewModels()


    private lateinit var timePickerTime : String

    private  lateinit var dayPickerDays : String

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


      binding.tvDay.setOnClickListener{ showAlertDialog() }



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

            if (picker.minute < 10) {
                timePickerTime =  h.toString().plus(":0").plus(min.toString())
                binding.startTimeField.text = timePickerTime
            } else {
                timePickerTime = "$h:$min"
                binding.startTimeField.text = timePickerTime
            }



        }
    }

    // DAY SELECTOR

    fun showAlertDialog() {

        var selectedDays : String


        val alertBuilder = activity?.let { MaterialAlertDialogBuilder(it) }
        val checkedArray: BooleanArray

        var daysList = mutableListOf<Int>()

        val daysArray = R.array.daysList
        val newArrayList =
            ArrayList<String>(Arrays.asList(*resources.getStringArray(R.array.daysList)))

        checkedArray = BooleanArray(newArrayList.size)


        alertBuilder?.setTitle("Select Day(s)")
        alertBuilder?.setCancelable(false)
        alertBuilder?.setMultiChoiceItems(
            daysArray,
            checkedArray,
            DialogInterface.OnMultiChoiceClickListener { _, index, checked ->
                if (checked) {

                    daysList.add(index)
                    Collections.sort(daysList)


                } else {

                    try {
                        daysList.removeAt(index)
                    } catch (e: Exception) {

                        Log.d("Exception", "FACED!")


                        for (j in checkedArray.indices) {
                            checkedArray[j] = false
                            daysList.clear()
                            selectedDays = ""
                            binding.tvDay.text = ""
                        }
                        Toast.makeText(
                            activity,
                            "Oops! Please tap 'Clear All' and try again",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            })

        alertBuilder?.setPositiveButton("OK", DialogInterface.OnClickListener { _, _ ->


            val stringBuilder = StringBuilder()

            for (j in daysList.indices) {


                stringBuilder.append(newArrayList[daysList[j]])

                if (j != daysList.size - 1) {
                    stringBuilder.append(", ")
                }
            }

            selectedDays = stringBuilder.toString()
            dayPickerDays = selectedDays

            Log.d("DAYS", selectedDays)

            binding.tvDay.text = dayPickerDays
            binding.tvDay.textSize = 15F

        })
        alertBuilder?.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ ->

            dialog.dismiss()


        })
        alertBuilder?.setNeutralButton("Clear All", DialogInterface.OnClickListener { _, _ ->
            for (j in checkedArray.indices) {
                checkedArray[j] = false
                daysList.clear()
                selectedDays = ""
                binding.tvDay.text = ""
            }

        })

        alertBuilder?.create()
        alertBuilder?.show()

    }








    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
       inflater.inflate(R.menu.add_batch_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.save_batch) {

            insertBatchToDb()

        }

        return super.onOptionsItemSelected(item)
    }


    // INSERT DATA TO DATABASE

    private fun insertBatchToDb() {

        val batchNameField = binding.batchNameField.editText?.text.toString()
        Log.d("Text grabbed", batchNameField)
        val timePickerTime = binding.timePickerButton.text.toString()
        val daysPicked = binding.tvDay.text.toString()

        val validation = sharedViewModel.verifyBatchInputData(batchNameField, timePickerTime, daysPicked)
        if (validation) {
            //Green Signal - Enter data to database
           val newBatchData = Batch(
               batchNameField,
               timePickerTime,
               daysPicked
           )

            studentViewModel.insertBatch(newBatchData)
            Toast.makeText(requireContext(),"Successfully added batch!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_addBatchFragment_to_dashboardFragment)
        } else {
            Toast.makeText(requireContext(),"Please fill out all fields.", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }




}
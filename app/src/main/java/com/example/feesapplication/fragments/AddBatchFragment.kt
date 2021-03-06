package com.example.feesapplication.fragments


import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.feesapplication.R
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.data.viewmodel.SharedViewModel
import com.example.feesapplication.data.viewmodel.StudentViewModel
import com.example.feesapplication.databinding.AddBatchFragmentBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat


class AddBatchFragment : Fragment() {

    private val studentViewModel: StudentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()


    private lateinit var timePickerTime: String

    private var dayPickerDays: String = ""

    private var _binding: AddBatchFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        // Set Menu
        setHasOptionsMenu(true)


        // Inflate the layout for this fragment
        _binding = AddBatchFragmentBinding.inflate(layoutInflater, container, false)

        binding.timePickerButton.setOnClickListener {
            openTimePicker()
        }


        binding.tvDay.setOnClickListener { showAlertDialog() }



        return binding.root

    }
    // TIME PICKER


    private fun openTimePicker() {
        val isSystem24Hour = is24HourFormat(requireContext())
        val clockFormat = if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H

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
                timePickerTime = h.toString().plus(":0").plus(min.toString())
                binding.startTimeField.text = timePickerTime
            } else {
                timePickerTime = "$h:$min"
                binding.startTimeField.text = timePickerTime
            }


        }
    }

    // DAY SELECTOR

    private fun showAlertDialog() {

        var selectedDays: String


        val alertBuilder = activity?.let { MaterialAlertDialogBuilder(it) }
        val checkedArray: BooleanArray

        val daysList = mutableListOf<Int>()

        val daysArray = R.array.daysList
        val newArrayList =
            ArrayList<String>(listOf(*resources.getStringArray(R.array.daysList)))

        checkedArray = BooleanArray(newArrayList.size)


        alertBuilder?.setTitle("Select Day(s)")
        alertBuilder?.setCancelable(false)
        alertBuilder?.setMultiChoiceItems(
            daysArray,
            checkedArray
        ) { _, index, checked ->
            if (checked) {

                daysList.add(index)
                daysList.sort()


            } else {


                daysList.drop(index)
                daysList.remove(index)
                checkedArray[index] = false


            }

        }

        alertBuilder?.setPositiveButton("Confirm") { _, _ ->


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

        }
        alertBuilder?.setNegativeButton("Cancel") { dialog, _ ->

            dialog.dismiss()


        }
        alertBuilder?.setNeutralButton("Clear All") { _, _ ->
            for (j in checkedArray.indices) {
                checkedArray[j] = false
                daysList.clear()
                selectedDays = ""
                binding.tvDay.text = ""
            }

        }

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
        val timePickerTime = binding.startTimeField.text.toString()
        // val daysPicked = binding.tvDay.text.toString()

        val validation =
            sharedViewModel.verifyBatchInputData(batchNameField, timePickerTime, dayPickerDays)
        if (validation) {
            //Green Signal - Enter data to database
            val newBatchData = Batch(
                batchNameField,
                timePickerTime,
                dayPickerDays
            )

            studentViewModel.insertBatch(newBatchData)
            findNavController().popBackStack()

        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT)
                .show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}
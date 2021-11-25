package com.example.feesapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.feesapplication.R
import com.example.feesapplication.data.database.FeeStatus
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.data.viewmodel.SharedViewModel
import com.example.feesapplication.data.viewmodel.StudentViewModel
import com.example.feesapplication.databinding.FragmentUpdateBinding
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*


class UpdateFragment : Fragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    private val studentViewModel: StudentViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    private var feeStatusSaved = "None"
    private lateinit var currentStudent: Student
    private lateinit var monthsSaved: String
    private lateinit var currentBatch: Batch

    private val MONTHS = listOf<String>(
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    )


    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!


    override fun onResume() {
        super.onResume()

        // Fees status field adapter
        val feesStatuses = resources.getStringArray(R.array.feesStatuses)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, feesStatuses)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUpdateBinding.inflate(layoutInflater, container, false)
        currentStudent = args.currentStudent
        studentViewModel.getBatchOfStudent(args.currentStudent.batchName)
            .observe(viewLifecycleOwner, {
                currentBatch = it
            })


        // Update pre-existing Checked Chips
        checkForCheckedChips()


        /*  val c = Calendar.getInstance()
                 val year = c.get(Calendar.YEAR)
                 val month = c.get(Calendar.MONTH)
                 val day = c.get(Calendar.DAY_OF_MONTH)

                 val dpd = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->


                     // Display Selected date
                     val date = "" + dayOfMonth + " " + MONTHS[monthOfYear] + ", " + year
                     Log.d("Day saved", date)

                 }, year, month, day)

                 dpd.show() */






        binding.studentNameField.editText?.setText(args.currentStudent.studentName)
        binding.contactNumberField.editText?.setText(args.currentStudent.studentNumber.toString())
        binding.feesField.editText?.setText(args.currentStudent.feesAmount.toString())
        binding.emailField.editText?.setText(args.currentStudent.studentEmail)
        binding.batchNameStudent.text = args.currentStudent.batchName
        binding.autoCompleteTextView.setText(args.currentStudent.feesStatus.toString(), false)


        var position = 0
        position = when (args.currentStudent.feesStatus) {
            FeeStatus.None -> {
                0
            }
            FeeStatus.Paid -> {
                1
            }
            FeeStatus.Unpaid -> {
                3
            }
        }

        binding.autoCompleteTextView.listSelection = position
        feeStatusSaved = binding.autoCompleteTextView.text.toString()
        binding.autoCompleteTextView.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                val selection = parent.getItemAtPosition(position) as String
                feeStatusSaved = selection
            }






        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_student_menu, menu)

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.update_student) {

            updateStudentToDb()

        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkForCheckedChips() {

        val stringRetrieved = args.currentStudent.monthsPaid
        Log.d("Show", "Tags $stringRetrieved")


        if (stringRetrieved.contains("January", true)) {
            binding.janChip.isChecked = true
        }
        if (stringRetrieved.contains("February", true)) {
            binding.febChip.isChecked = true
        }
        if (stringRetrieved.contains("March", true)) {
            binding.marChip.isChecked = true
        }
        if (stringRetrieved.contains("April", true)) {
            binding.aprChip.isChecked = true
        }
        if (stringRetrieved.contains("May", true)) {
            binding.mayChip.isChecked = true
        }
        if (stringRetrieved.contains("June", true)) {
            binding.juneChip.isChecked = true
        }
        if (stringRetrieved.contains("July", true)) {
            binding.julChip.isChecked = true
        }
        if (stringRetrieved.contains("August", true)) {
            binding.augChip.isChecked = true
        }
        if (stringRetrieved.contains("September", true)) {
            binding.sepChip.isChecked = true
        }
        if (stringRetrieved.contains("October", true)) {
            binding.octChip.isChecked = true
        }
        if (stringRetrieved.contains("November", true)) {
            binding.novChip.isChecked = true
        }
        if (stringRetrieved.contains("December", true)) {
            binding.decChip.isChecked = true
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getDefault())

        calendar.timeInMillis = today

        calendar[Calendar.MONTH] = Calendar.JANUARY
        val jan = calendar.timeInMillis

        calendar.timeInMillis = today
        calendar[Calendar.MONTH] = Calendar.DECEMBER
        val dec = calendar.timeInMillis

        calendar[Calendar.MONTH] = Calendar.FEBRUARY
        val feb = calendar.timeInMillis

        calendar[Calendar.MONTH] = Calendar.MARCH
        val mar = calendar.timeInMillis

        calendar[Calendar.MONTH] = Calendar.APRIL
        val apr = calendar.timeInMillis

        calendar[Calendar.MONTH] = Calendar.MAY
        val may = calendar.timeInMillis

        calendar[Calendar.MONTH] = Calendar.JUNE
        val jun = calendar.timeInMillis

        calendar[Calendar.MONTH] = Calendar.JULY
        val jul = calendar.timeInMillis

        calendar[Calendar.MONTH] = Calendar.AUGUST
        val aug = calendar.timeInMillis

        calendar[Calendar.MONTH] = Calendar.SEPTEMBER
        val sep = calendar.timeInMillis

        calendar[Calendar.MONTH] = Calendar.OCTOBER
        val oct = calendar.timeInMillis

        calendar[Calendar.MONTH] = Calendar.NOVEMBER
        val nov = calendar.timeInMillis

        //Months according to Chips checked
        binding.chipGroup.children.forEach {
            (it as Chip).setOnCheckedChangeListener { buttonView, isChecked ->

                if (it.isChecked) {
                    when (it.id) {
                        R.id.janChip -> {
                            val constraintsBuilder =
                                CalendarConstraints.Builder()
                                    .setStart(jan)
                                    .setOpenAt(jan)
                                    .setEnd(jan)

                            val datePicker = MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Date Selection")
                                .setCalendarConstraints(constraintsBuilder.build())
                                .build()

                            datePicker.show(requireActivity().supportFragmentManager, "tag")
                        }
                        R.id.febChip -> {
                            val constraintsBuilder =
                                CalendarConstraints.Builder()
                                    .setStart(feb)
                                    .setOpenAt(feb)
                                    .setEnd(feb)

                            val datePicker = MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Date Selection")
                                .setCalendarConstraints(constraintsBuilder.build())
                                .build()

                            datePicker.show(requireActivity().supportFragmentManager, "tag")
                        }
                        R.id.marChip -> {
                            val constraintsBuilder =
                                CalendarConstraints.Builder()
                                    .setStart(mar)
                                    .setOpenAt(mar)
                                    .setEnd(mar)

                            val datePicker = MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Date Selection")
                                .setCalendarConstraints(constraintsBuilder.build())
                                .build()

                            datePicker.show(requireActivity().supportFragmentManager, "tag")
                        }
                        R.id.aprChip -> {
                            val constraintsBuilder =
                                CalendarConstraints.Builder()
                                    .setStart(apr)
                                    .setOpenAt(apr)
                                    .setEnd(apr)

                            val datePicker = MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Date Selection")
                                .setCalendarConstraints(constraintsBuilder.build())
                                .build()

                            datePicker.show(requireActivity().supportFragmentManager, "tag")
                        }
                        R.id.mayChip -> {
                            val constraintsBuilder =
                                CalendarConstraints.Builder()
                                    .setStart(may)
                                    .setOpenAt(may)
                                    .setEnd(may)

                            val datePicker = MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Date Selection")
                                .setCalendarConstraints(constraintsBuilder.build())
                                .build()

                            datePicker.show(requireActivity().supportFragmentManager, "tag")
                        }
                        R.id.juneChip -> {
                            val constraintsBuilder =
                                CalendarConstraints.Builder()
                                    .setStart(jun)
                                    .setOpenAt(jun)
                                    .setEnd(jun)

                            val datePicker = MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Date Selection")
                                .setCalendarConstraints(constraintsBuilder.build())
                                .build()

                            datePicker.show(requireActivity().supportFragmentManager, "tag")
                        }
                        R.id.julChip -> {
                            val constraintsBuilder =
                                CalendarConstraints.Builder()
                                    .setStart(jul)
                                    .setOpenAt(jul)
                                    .setEnd(jul)

                            val datePicker = MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Date Selection")
                                .setCalendarConstraints(constraintsBuilder.build())
                                .build()

                            datePicker.show(requireActivity().supportFragmentManager, "tag")
                        }
                        R.id.augChip -> {
                            val constraintsBuilder =
                                CalendarConstraints.Builder()
                                    .setStart(jun)
                                    .setOpenAt(jun)
                                    .setEnd(jun)

                            val datePicker = MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Date Selection")
                                .setCalendarConstraints(constraintsBuilder.build())
                                .build()

                            datePicker.show(requireActivity().supportFragmentManager, "tag")
                        }
                        R.id.sepChip -> {
                            val constraintsBuilder =
                                CalendarConstraints.Builder()
                                    .setStart(sep)
                                    .setOpenAt(sep)
                                    .setEnd(sep)

                            val datePicker = MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Date Selection")
                                .setCalendarConstraints(constraintsBuilder.build())
                                .build()

                            datePicker.show(requireActivity().supportFragmentManager, "tag")
                        }
                        R.id.octChip -> {
                            val constraintsBuilder =
                                CalendarConstraints.Builder()
                                    .setStart(oct)
                                    .setOpenAt(oct)
                                    .setEnd(oct)

                            val datePicker = MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Date Selection")
                                .setCalendarConstraints(constraintsBuilder.build())
                                .build()

                            datePicker.show(requireActivity().supportFragmentManager, "tag")
                        }
                        R.id.novChip -> {
                            val constraintsBuilder =
                                CalendarConstraints.Builder()
                                    .setStart(nov)
                                    .setOpenAt(nov)
                                    .setEnd(nov)

                            val datePicker = MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Date Selection")
                                .setCalendarConstraints(constraintsBuilder.build())
                                .build()

                            datePicker.show(requireActivity().supportFragmentManager, "tag")
                        }
                        R.id.decChip -> {
                            val constraintsBuilder =
                                CalendarConstraints.Builder()
                                    .setStart(dec)
                                    .setOpenAt(dec)
                                    .setEnd(dec)

                            val datePicker = MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Date Selection")
                                .setCalendarConstraints(constraintsBuilder.build())
                                .build()

                            datePicker.show(requireActivity().supportFragmentManager, "tag")
                        }


                    }


                }
            }
        }
    }


    private fun updateStudentToDb() {

        val stringMonths = StringBuilder()

        binding.chipGroup.checkedChipIds.forEach {
            when {
                it.equals(binding.janChip.id) -> {
                    stringMonths.append("January, ")
                }
                it.equals(binding.febChip.id) -> {
                    stringMonths.append("February, ")
                }
                it.equals(binding.marChip.id) -> {
                    stringMonths.append("March, ")
                }
                it.equals(binding.aprChip.id) -> {
                    stringMonths.append("April, ")
                }
                it.equals(binding.mayChip.id) -> {
                    stringMonths.append("May, ")
                }
                it.equals(binding.juneChip.id) -> {
                    stringMonths.append("June, ")
                }
                it.equals(binding.julChip.id) -> {
                    stringMonths.append("July, ")
                }
                it.equals(binding.augChip.id) -> {
                    stringMonths.append("August, ")
                }
                it.equals(binding.sepChip.id) -> {
                    stringMonths.append("September, ")
                }
                it.equals(binding.octChip.id) -> {
                    stringMonths.append("October, ")
                }
                it.equals(binding.novChip.id) -> {
                    stringMonths.append("November, ")
                }
                it.equals(binding.decChip.id) -> {
                    stringMonths.append("December")
                }

                else -> {
                    stringMonths.append("Empty")
                }

            }
        }




        monthsSaved = stringMonths.toString()

        val studentNameField = binding.studentNameField.editText?.text.toString()
        val contactNumber = binding.contactNumberField.editText?.text.toString()
        val feesField = binding.feesField.editText?.text.toString()
        val emailField = binding.emailField.editText?.text.toString()


        val validation = sharedViewModel.verifyStudentInputData(
            studentNameField,
            contactNumber,
            feesField,
            feeStatusSaved,
            emailField
        )
        if (validation) {
            // Green signal to save data into database
            val newStudentData = Student(
                studentName = studentNameField,
                studentNumber = contactNumber.toLong(),
                feesAmount = feesField.toLong(),
                feesStatus = sharedViewModel.parseFeesStatus(feeStatusSaved),
                studentEmail = emailField,
                batchName = args.currentStudent.batchName,
                monthsPaid = monthsSaved

            )

            studentViewModel.updateStudent(newStudentData)
            Toast.makeText(requireContext(), "Successfully updated student!", Toast.LENGTH_SHORT)
                .show()

            val action =
                UpdateFragmentDirections.actionUpdateFragmentToStudentListFragment(currentBatch)
            findNavController().navigate(action)

        } else {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT)
                .show()
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
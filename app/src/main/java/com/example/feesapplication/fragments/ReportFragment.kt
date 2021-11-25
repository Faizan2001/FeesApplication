package com.example.feesapplication.fragments


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.feesapplication.R
import com.example.feesapplication.data.viewmodel.StudentViewModel
import com.example.feesapplication.databinding.ReportFragmentBinding
import com.example.feesapplication.list.utils.hideKeyboard
import java.io.File
import java.util.*


class ReportFragment : Fragment() {

    private val studentViewModel: StudentViewModel by viewModels()


    private var _binding: ReportFragmentBinding? = null
    private val binding get() = _binding!!

    private var reportGathered: String = ""
    private lateinit var myFile: File


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = ReportFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val mCalendar = Calendar.getInstance()
        val month: String = mCalendar.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG,
            Locale.getDefault()
        )




        binding.titleText.text = "Month of $month"

        //Create and save updated File
        saveReportInDeviceStorage()

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

        when (item.itemId) {
            R.id.save_report -> {
                val sendIntent = Intent(Intent.ACTION_SEND)
                sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(myFile))
                sendIntent.type = "text/csv"
                startActivity(Intent.createChooser(sendIntent, "Save/Share File"))

                Log.d("TAG", Uri.fromFile(myFile).toString())

            }
        }


        return super.onOptionsItemSelected(item)
    }


    private fun saveReportInDeviceStorage() {


        val HEADER = "Name,Number,Email,Batch,Fees,Fees_Status "

        val mCalendar = Calendar.getInstance()
        val month: String = mCalendar.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG,
            Locale.getDefault()
        )

        var filename = "${month}Report.csv"

        var path = context?.getExternalFilesDir(null)
        Log.d("PATH", path.toString())//get file directory for this package
        //(Android/data/.../files | ... is your app package)

        //create fileOut object
        val fileOut = File(path, filename)

        //delete any file object with path and filename that already exists
        fileOut.delete()

        //create a new file
        fileOut.createNewFile()


        studentViewModel.getAllStudentData.observe(viewLifecycleOwner, Observer { student ->
            fileOut.appendText(HEADER)
            val stringBuilder = StringBuilder()
            var totalFees: Double = 0.0


            for (i in student.indices) {

                fileOut.appendText("\n")
                fileOut.appendText("${(i) + 1}," + student[i].studentName + "," + student[i].studentNumber + "," + student[i].studentEmail + "," + student[i].batchName + "," + student[i].feesAmount + "," + student[i].feesStatus)
                stringBuilder.append("${(i) + 1}) " + student[i].studentName + " [" + student[i].batchName + "]: Current Status : " + student[i].feesStatus.toString() + ", " + student[i].feesAmount.toString() + "\n" + "Months Paid: " + student[i].monthsPaid + "\n" + "\n" + "Contact Number: " + student[i].studentNumber.toString() + ", Mail: " + student[i].studentEmail)
                stringBuilder.append("\n")
                stringBuilder.append("\n")
                stringBuilder.append("━━━━━━━━━━━━━")
                stringBuilder.append("\n")
                stringBuilder.append("\n")
                if (student[i].feesStatus.toString().contains("Paid", false)) {
                    totalFees += student[i].feesAmount
                }


            }

            reportGathered = stringBuilder.toString()
            binding.totalFees.text = "Total Fees Paid: $totalFees"
            binding.reportInfo.text = (reportGathered)

        })

        studentViewModel.getPaidCount.observe(viewLifecycleOwner, Observer {
            binding.paidFees.text = "Paid: $it"
        })
        studentViewModel.getUnpaidCount.observe(viewLifecycleOwner, Observer {
            binding.unpaidFees.text = "Unpaid: $it"
        })


        //append the header and a newline

        fileOut.appendText("\n")
        fileOut.appendText("REPORT")
        fileOut.appendText("\n")

        Log.d("WORKING", "YES")

        myFile = fileOut


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

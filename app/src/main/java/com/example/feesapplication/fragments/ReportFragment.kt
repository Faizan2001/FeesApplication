package com.example.feesapplication.fragments


import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.feesapplication.R
import com.example.feesapplication.data.viewmodel.StudentViewModel
import com.example.feesapplication.databinding.ReportFragmentBinding
import com.example.feesapplication.list.utils.hideKeyboard
import com.example.feesapplication.list.utils.observeOnce
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


class ReportFragment : Fragment() {

    private val studentViewModel: StudentViewModel by viewModels()


    private var _binding: ReportFragmentBinding? = null
    private val binding get() = _binding!!


    private var reportGathered: String = ""



    private fun checkPermissions() {

     val hasReadPermission = ContextCompat.checkSelfPermission(requireActivity(),
           Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED

        val hasWritePermission = ContextCompat.checkSelfPermission(requireActivity(),
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED


        if (!hasReadPermission && !hasWritePermission) {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE), PackageManager.PERMISSION_GRANTED) }
    }


    private fun createPdf() {




         val doc = Document()
        val mCalendar = Calendar.getInstance()
        val month: String = mCalendar.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG,
            Locale.getDefault()
        )


        try {
            val path =  Environment.getExternalStorageDirectory().path + "/Download/$month Report by.pdf"
            val file = File(path)
            val fOut = FileOutputStream(file)
            PdfWriter.getInstance(doc, fOut)

            //open the document
            doc.open()


            val p1 = Paragraph(
                "Thank you for using Feenance\n",
                FontFactory.getFont("Times New Roman", 15f, Font.UNDERLINE)
            )

            val p2 = Paragraph(
                "$month Report\n\n",
                FontFactory.getFont("Times New Roman", 30f, Font.BOLDITALIC)
            )

            p1.alignment = Paragraph.ALIGN_CENTER
            p2.alignment = Paragraph.ALIGN_CENTER



            doc.add(p1)
            doc.add(p2)

            val table = PdfPTable(12)
            table.widthPercentage = 110f


            val cell1 = PdfPCell(Phrase("Name"))
            cell1.colspan = 2
            val cell2 = PdfPCell(Phrase("Batch"))
            cell2.colspan = 2
            val cell3 = PdfPCell(Phrase("This Month's status"))
            val cell4 = PdfPCell(Phrase("Fees"))
            val cell5 = PdfPCell(Phrase("Months Paid"))
            cell5.colspan = 2
            val cell6 = PdfPCell(Phrase("Number"))
            cell6.colspan = 2
            val cell7 = PdfPCell(Phrase("Email"))
            cell7.colspan = 2


            cell1.verticalAlignment = Element.ALIGN_MIDDLE
            cell1.horizontalAlignment = Element.ALIGN_CENTER

            cell2.verticalAlignment = Element.ALIGN_MIDDLE
            cell2.horizontalAlignment = Element.ALIGN_CENTER

            cell3.verticalAlignment = Element.ALIGN_MIDDLE
            cell3.horizontalAlignment = Element.ALIGN_CENTER

            cell4.verticalAlignment = Element.ALIGN_MIDDLE
            cell4.horizontalAlignment = Element.ALIGN_CENTER

            cell5.verticalAlignment = Element.ALIGN_MIDDLE
            cell5.horizontalAlignment = Element.ALIGN_CENTER

            cell6.verticalAlignment = Element.ALIGN_MIDDLE
            cell6.horizontalAlignment = Element.ALIGN_CENTER

            cell7.verticalAlignment = Element.ALIGN_MIDDLE
            cell7.horizontalAlignment = Element.ALIGN_CENTER






            table.addCell(cell1)
            table.addCell(cell2)
            table.addCell(cell3)
            table.addCell(cell4)
            table.addCell(cell5)
            table.addCell(cell6)
            table.addCell(cell7)
            studentViewModel.getAllStudentData.observeOnce(
                viewLifecycleOwner,
                { students ->

                    if (students != null) {
                        for (i in students.indices) {
                            val cell1 = PdfPCell(Phrase(students[i].studentName))
                            cell1.colspan = 2
                            val cell2 = PdfPCell(Phrase(students[i].batchName))
                            cell2.colspan = 2
                            val cell3 = PdfPCell(Phrase(students[i].feesStatus.toString()))
                            val cell4 = PdfPCell(Phrase(students[i].feesAmount.toString()))
                            val cell5 = PdfPCell(Phrase(students[i].monthsPaid))
                            cell5.colspan = 2
                            val cell6 = PdfPCell(Phrase(students[i].studentNumber.toString()))
                            cell6.colspan = 2
                            val cell7 = PdfPCell(Phrase(students[i].studentEmail))
                            cell7.colspan = 2


                            table.addCell(cell1)
                            table.addCell(cell2)
                            table.addCell(cell3)
                            table.addCell(cell4)
                            table.addCell(cell5)
                            table.addCell(cell6)
                            table.addCell(cell7)
                        }

                    }


                })


            // Header
            // Header


            doc.add(table)


            //add paragraph to document
        } catch (de: DocumentException) {
            Log.e("PDFCreator", "DocumentException:$de")
        } catch (e: IOException) {
            Log.e("PDFCreator", "ioException:$e")
        } finally {
            doc.close()
        }
    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = ReportFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        val mCalendar = Calendar.getInstance()
        val month: String = mCalendar.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG,
            Locale.getDefault()
        )


        checkPermissions()




        binding.titleText.text = "Month of $month"


        studentViewModel.getAllStudentData.observe(viewLifecycleOwner, { student ->


            val stringBuilder = StringBuilder()
            var totalFees = 0.0



            for (i in student.indices) {

                // fileOut.appendText("\n")
                // fileOut.appendText("${(i) + 1}," + student[i].studentName + "," + student[i].studentNumber + "," + student[i].studentEmail + "," + student[i].batchName + "," + student[i].feesAmount + "," + student[i].feesStatus)
                stringBuilder.append("${(i) + 1}) " + student[i].studentName + " [" + student[i].batchName + "] \n Current Status : " + student[i].feesStatus.toString() + ", " + student[i].feesAmount.toString() + "\n" + "Months Paid: " + student[i].monthsPaid + "\n" + "\n" + "Contact Number: " + student[i].studentNumber.toString() + "\nMail: " + student[i].studentEmail)
                stringBuilder.append("\n")
                stringBuilder.append("\n")
                stringBuilder.append("━━━━━━━━━━━━━━━━━━━━")
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

        studentViewModel.getPaidCount.observe(viewLifecycleOwner, {
            binding.paidFees.text = "Paid: $it"
        })
        studentViewModel.getUnpaidCount.observe(viewLifecycleOwner, {
            binding.unpaidFees.text = "Unpaid: $it"
        })


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

                createPdf()
              if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                  viewPdf()
              } else if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                  Toast.makeText(context, "Pdf successfully saved in your File Manager- Downloads Folder", Toast.LENGTH_SHORT).show()
              }
            }
        }


        return super.onOptionsItemSelected(item)
    }
    ////



    // Views PDF if API < 29
    private fun viewPdf() {

        val mCalendar = Calendar.getInstance()
        val month: String = mCalendar.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG,
            Locale.getDefault()
        )


        val pdfFile = File(Environment.getExternalStorageDirectory().path + "/Download/$month Report.pdf")
        val path = Uri.fromFile(pdfFile)

        // Setting the intent for pdf reader
        val pdfIntent = Intent(Intent.ACTION_VIEW)
        pdfIntent.setDataAndType(path, "application/pdf")
        pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        pdfIntent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION

        startActivity(pdfIntent)



    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

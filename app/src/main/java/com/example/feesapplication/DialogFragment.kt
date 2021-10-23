package com.example.feesapplication

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList


class DayPickerDialogFragment : DialogFragment() {



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        var selectedDays: String

        return activity?.let {
            val alertBuilder = MaterialAlertDialogBuilder(it)
            val checkedArray : BooleanArray

            var daysList = mutableListOf<Int>()

            val daysArray = R.array.daysList
            val newArrayList = ArrayList<String>(Arrays.asList(*resources.getStringArray(R.array.daysList)))

            checkedArray = BooleanArray(newArrayList.size)


            alertBuilder.setTitle("Select Day(s)")
            alertBuilder.setCancelable(false)
            alertBuilder.setMultiChoiceItems(daysArray, checkedArray, DialogInterface.OnMultiChoiceClickListener {
                _, index, checked ->
                if(checked) {

                   daysList.add(index)
                    Collections.sort(daysList)


                } else {

                   try { daysList.removeAt(index) } catch (e : Exception) {

                       Log.d("Exception", "FACED!")


                       for (j in checkedArray.indices) {
                           checkedArray[j] = false
                           daysList.clear()
                           selectedDays = ""
                       }
                       Toast.makeText(activity, "Oops! Please tap 'Clear All' and try again", Toast.LENGTH_SHORT).show()
                       }
                   }

            })

            alertBuilder.setPositiveButton("OK", DialogInterface.OnClickListener{ _, _ ->


                val stringBuilder = StringBuilder()

                for (j in daysList.indices) {


                    stringBuilder.append(newArrayList[daysList[j]])

                    if (j != daysList.size - 1) {
                        stringBuilder.append(", ")
                    }
                }

               selectedDays = stringBuilder.toString()

                Log.d("DAYS", selectedDays)



            })
            alertBuilder.setNegativeButton("Cancel", DialogInterface.OnClickListener{ dialog,_ ->

                dialog.dismiss()

            })
            alertBuilder.setNeutralButton("Clear All", DialogInterface.OnClickListener{ _,_ ->
                for (j in checkedArray.indices) {
                    checkedArray[j] = false
                    daysList.clear()
                    selectedDays = ""
                }
            })

            alertBuilder.create()

        } ?: throw IllegalStateException("Exception!! Activity is null")


    }



}



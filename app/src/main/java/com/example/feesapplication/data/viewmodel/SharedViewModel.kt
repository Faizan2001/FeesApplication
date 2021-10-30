package com.example.feesapplication.data.viewmodel

import android.app.Application
import android.graphics.Color.green
import android.graphics.Color.red
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import com.example.feesapplication.MainActivity
import com.example.feesapplication.R
import com.example.feesapplication.data.FeeStatus

class SharedViewModel(application: Application) : AndroidViewModel(application) {




    fun verifyBatchInputData(vbatcNameField: String, vtimePickerTime: String, vdaysPicked: String): Boolean {

        return if(TextUtils.isEmpty(vbatcNameField) || TextUtils.isEmpty(vtimePickerTime) || TextUtils.isEmpty(vdaysPicked))
        {
            false
        } else !(vbatcNameField.isEmpty() || vtimePickerTime.isEmpty() || vdaysPicked.isEmpty())


    }

    fun verifyStudentInputData(vStudentNameField: String, vContactNumberField: String, vFeesField: String, vFeesStatus: String, vEmailField : String): Boolean {

        return if(TextUtils.isEmpty(vStudentNameField) || TextUtils.isEmpty(vContactNumberField) || TextUtils.isEmpty(vFeesField) || TextUtils.isEmpty(vFeesStatus)
            || TextUtils.isEmpty(vEmailField))
        {
            false
        } else !(vStudentNameField.isEmpty() || vContactNumberField.isEmpty() || vFeesField.isEmpty() || vFeesStatus.isEmpty() || vEmailField.isEmpty())


    }

    fun parseFeesStatus(feesStatus : String): FeeStatus {
        return when(feesStatus){
            "None" -> {
                FeeStatus.NONE}
            "Paid" -> {
                FeeStatus.PAID}
            "Unpaid" -> {
                FeeStatus.UNPAID}

            else -> FeeStatus.NONE
        }
    }
}
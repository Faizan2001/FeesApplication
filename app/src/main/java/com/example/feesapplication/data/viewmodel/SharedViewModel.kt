package com.example.feesapplication.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.feesapplication.data.database.FeeStatus
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.data.database.entities.Student

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    /** ============================= Dashboard Fragment ============================= */

    val emptyBatchDatabase: MutableLiveData<Boolean> = MutableLiveData(false)

    fun checkIfBatchDatabaseEmpty(batch: List<Batch>) {
        emptyBatchDatabase.value = batch.isEmpty()
    }

    fun verifyBatchInputData(
        vBatcNameField: String,
        vTimePickerTime: String,
        vDaysPicked: String
    ): Boolean {

        return !(vBatcNameField.isEmpty() || vTimePickerTime.isEmpty() || vDaysPicked.isEmpty())

    }

    /** ============================= Student Fragment ============================= */


    val emptyStudentDatabase: MutableLiveData<Boolean> = MutableLiveData(false)


    fun checkIfStudentDatabaseEmpty(student: List<Student>) {
        emptyStudentDatabase.value = student.isEmpty()
    }

    fun verifyStudentInputData(
        vStudentNameField: String,
        vContactNumberField: String,
        vFeesField: String,
        vFeesStatus: String,
        vEmailField: String
    ): Boolean {

        return !(vStudentNameField.isEmpty() || vContactNumberField.isEmpty() || vFeesField.isEmpty() || vFeesStatus.isEmpty() || vEmailField.isEmpty())


    }

    fun parseFeesStatus(feesStatus: String): FeeStatus {
        return when (feesStatus) {
            "None" -> {
                FeeStatus.None
            }
            "Paid" -> {
                FeeStatus.Paid
            }
            "Unpaid" -> {
                FeeStatus.Unpaid
            }

            else -> FeeStatus.None
        }
    }
}
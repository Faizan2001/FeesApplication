package com.example.feesapplication

import android.content.ClipData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.feesapplication.data.Student
import com.example.feesapplication.data.StudentDao
import kotlinx.coroutines.launch

class StudentViewModel(private val studentDao: StudentDao) : ViewModel() {

    private fun insertStudentInfo(student: Student) {

        viewModelScope.launch {
            studentDao.insert(student)
        }
    }

 /*   private fun getNewItemEntry(studentName: String, studentNumber: Int, studentEmail: String, feesStatus : Boolean, feesAmount: Int): Student {
        return Student(
            studentName  = "Hello",
            studentNumber = 0213123123,
            studentEmail = "hqwehqweh@gmail.com",
            feesStatus = True,
            feesAmount = feesAmount
        )
    } */

}

class StudentViewModelFactory(private val studentDao: StudentDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentViewModel(studentDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }


}

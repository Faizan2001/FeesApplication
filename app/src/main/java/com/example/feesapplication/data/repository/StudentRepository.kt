package com.example.feesapplication.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.data.database.StudentDao
import com.example.feesapplication.data.database.entities.Batch

class StudentRepository(private val studentDao: StudentDao) {

    val getAllStudentData: LiveData<List<Student>> = studentDao.getAllStudentData()
    val getAllBatchData : LiveData<List<Batch>> = studentDao.getAllBatchData()


    suspend fun insertStudent(student: Student){
      studentDao.insertStudent(student)

    }

    suspend fun insertBatch(batch: Batch) {
        studentDao.insertBatch(batch)
    }

    fun studentsInBatch(batchName : String) : LiveData<List<Student>> {
        return studentDao.getBatchWithStudents(batchName)
    }




}
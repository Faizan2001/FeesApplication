package com.example.feesapplication.data.repository

import androidx.lifecycle.LiveData
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.data.database.StudentDao
import com.example.feesapplication.data.database.entities.Batch

class StudentRepository(private val studentDao: StudentDao) {

    val getAllData: LiveData<List<Student>> = studentDao.getAllData()

    suspend fun insertStudent(student: Student){
      studentDao.insertStudent(student)

    }

    suspend fun insertBatch(batch: Batch) {
        studentDao.insertBatch(batch)
    }



}
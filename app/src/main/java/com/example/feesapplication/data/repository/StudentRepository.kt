package com.example.feesapplication.data.repository

import androidx.lifecycle.LiveData
import com.example.feesapplication.data.database.Student
import com.example.feesapplication.data.database.StudentDao

class StudentRepository(private val studentDao: StudentDao) {

    val getAllData: LiveData<List<Student>> = studentDao.getAllData()

    suspend fun insert(student: Student){
      studentDao.insert(student)

    }



}
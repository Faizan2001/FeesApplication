package com.example.feesapplication.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.data.database.StudentDao
import com.example.feesapplication.data.database.entities.Batch
import kotlinx.coroutines.flow.Flow

class StudentRepository(private val studentDao: StudentDao) {

    val getAllStudentData: LiveData<List<Student>> = studentDao.getAllStudentData()
    val getAllBatchData : LiveData<List<Batch>> = studentDao.getAllBatchData()

    val sortByUnpaid: LiveData<List<Student>> = studentDao.sortByUnpaid()
    val sortByPaid: LiveData<List<Student>> = studentDao.sortByPaid()

    val sortBySunday: LiveData<List<Batch>> = studentDao.sortBySunday()
    val sortByMonday: LiveData<List<Batch>> = studentDao.sortByMonday()
    val sortByTuesday: LiveData<List<Batch>> = studentDao.sortByTuesday()
    val sortByWednesday: LiveData<List<Batch>> = studentDao.sortByWednesday()
    val sortByThursday: LiveData<List<Batch>> = studentDao.sortByThursday()
    val sortByFriday: LiveData<List<Batch>> = studentDao.sortByFriday()
    val sortBySaturday: LiveData<List<Batch>> = studentDao.sortBySaturday()

    suspend fun insertStudent(student: Student){
      studentDao.insertStudent(student)

    }

    suspend fun insertBatch(batch: Batch) {
        studentDao.insertBatch(batch)
    }

    suspend fun deleteStudent(student: Student) {
        studentDao.deleteStudent(student)
    }

    suspend fun deleteBatch(batch: Batch) {
        studentDao.deleteBatch(batch)
    }

    suspend fun deleteAllStudents(batchName: String) {
        studentDao.deleteAllStudents(batchName)
    }

    fun studentsInBatch(batchName : String) : LiveData<List<Student>> {
        return studentDao.getBatchWithStudents(batchName)
    }

    fun searchBatchDatabase(searchQuery: String): LiveData<List<Batch>> {
        return studentDao.searchBatchDatabase(searchQuery)
    }

    fun searchStudentInBatch(vBatchName: String, searchQuery: String): LiveData<List<Student>> {
        return studentDao.searchStudentInBatch(vBatchName, searchQuery)
    }

    fun searchStudentDatabase(searchQuery: String): LiveData<List<Student>> {
        return studentDao.searchStudentDatabase(searchQuery)
    }








}
package com.example.feesapplication.data.repository

import androidx.lifecycle.LiveData
import com.example.feesapplication.data.database.StudentDao
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.data.database.entities.Student

class StudentRepository(private val studentDao: StudentDao) {

    val getAllStudentData: LiveData<List<Student>> = studentDao.getAllStudentData()
    val getAllBatchData: LiveData<List<Batch>> = studentDao.getAllBatchData()

    val getBatchCount: LiveData<Int> = studentDao.getBatchCount()
    val getStudentCount: LiveData<Int> = studentDao.getStudentCount()
    val getUnpaidCount: LiveData<Int> = studentDao.getUnpaidCount()
    val getPaidCount: LiveData<Int> = studentDao.getPaidCount()

    val sortByUnpaid: LiveData<List<Student>> = studentDao.sortByUnpaid()
    val sortByPaid: LiveData<List<Student>> = studentDao.sortByPaid()

    val sortBySunday: LiveData<List<Batch>> = studentDao.sortBySunday()
    val sortByMonday: LiveData<List<Batch>> = studentDao.sortByMonday()
    val sortByTuesday: LiveData<List<Batch>> = studentDao.sortByTuesday()
    val sortByWednesday: LiveData<List<Batch>> = studentDao.sortByWednesday()
    val sortByThursday: LiveData<List<Batch>> = studentDao.sortByThursday()
    val sortByFriday: LiveData<List<Batch>> = studentDao.sortByFriday()
    val sortBySaturday: LiveData<List<Batch>> = studentDao.sortBySaturday()


    suspend fun insertStudent(student: Student) {
        studentDao.insertStudent(student)

    }

    suspend fun insertBatch(batch: Batch) {
        studentDao.insertBatch(batch)
    }

    suspend fun updateStudent(student: Student) {
        studentDao.update(student)
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

    fun getBatchOfStudent(batchName: String): LiveData<Batch> {
        return studentDao.getBatchOfStudent(batchName)
    }

    fun studentsInBatch(batchName: String): LiveData<List<Student>> {
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

    fun sortByBatchUnpaid(batchName: String): LiveData<List<Student>> {
        return studentDao.sortByBatchUnpaid(batchName)
    }

    fun sortByBatchPaid(batchName: String): LiveData<List<Student>> {
        return studentDao.sortByBatchPaid(batchName)
    }


}
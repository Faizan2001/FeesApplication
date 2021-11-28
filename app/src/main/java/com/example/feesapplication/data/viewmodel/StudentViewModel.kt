package com.example.feesapplication.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.feesapplication.data.database.StudentRoomDatabase
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.data.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentViewModel(application: Application) : AndroidViewModel(application) {

    private val studentDao = StudentRoomDatabase.getDatabase(application).studentDao()
    private val repository: StudentRepository = StudentRepository(studentDao)

    val getAllStudentData: LiveData<List<Student>> = repository.getAllStudentData
    val getAllBatchData: LiveData<List<Batch>> = repository.getAllBatchData

    val getBatchCount: LiveData<Int> = repository.getBatchCount
    val getStudentCount: LiveData<Int> = repository.getStudentCount
    val getUnpaidCount: LiveData<Int> = repository.getUnpaidCount
    val getPaidCount: LiveData<Int> = repository.getPaidCount

    val sortByUnpaid: LiveData<List<Student>> = repository.sortByUnpaid
    val sortByPaid: LiveData<List<Student>> = repository.sortByPaid

    val sortBySunday: LiveData<List<Batch>> = repository.sortBySunday
    val sortByMonday: LiveData<List<Batch>> = repository.sortByMonday
    val sortByTuesday: LiveData<List<Batch>> = repository.sortByTuesday
    val sortByWednesday: LiveData<List<Batch>> = repository.sortByWednesday
    val sortByThursday: LiveData<List<Batch>> = repository.sortByThursday
    val sortByFriday: LiveData<List<Batch>> = repository.sortByFriday
    val sortBySaturday: LiveData<List<Batch>> = repository.sortBySaturday


    fun insertBatch(batch: Batch) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.insertBatch(batch)
        }

    }

    fun insertStudent(student: Student) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.insertStudent(student)
        }

    }

    fun updateStudent(student: Student) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.updateStudent(student)
        }
    }

    fun studentsInBatch(batchName: String): LiveData<List<Student>> {
        return repository.studentsInBatch(batchName)
    }

    fun deleteAllStudents(batchName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllStudents(batchName)
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteStudent(student)
        }
    }

    fun deleteBatch(batch: Batch) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBatch(batch)
        }
    }

    fun searchBatchDatabase(searchQuery: String): LiveData<List<Batch>> {
        return repository.searchBatchDatabase(searchQuery)
    }

    fun searchStudentInBatch(vBatchName: String, searchQuery: String): LiveData<List<Student>> {
        return repository.searchStudentInBatch(vBatchName, searchQuery)
    }

    fun searchStudentDatabase(searchQuery: String): LiveData<List<Student>> {
        return repository.searchStudentDatabase(searchQuery)
    }

    fun getBatchOfStudent(vBatchName: String): LiveData<Batch> {
        return repository.getBatchOfStudent(vBatchName)
    }

    fun sortByBatchPaid(vBatchName: String): LiveData<List<Student>> {
        return repository.sortByBatchPaid(vBatchName)
    }

    fun sortByBatchUnpaid(vBatchName: String): LiveData<List<Student>> {
        return repository.sortByBatchUnpaid(vBatchName)
    }


}
